package com.example.msaccount.service.impl;

import com.example.msaccount.config.KeycloakProvider;
import com.example.msaccount.customExceptions.*;
import com.example.msaccount.mapper.KeycloakMapper;
import com.example.msaccount.model.dto.KeycloakUserDTO;
import com.example.msaccount.model.request.admin.AccountCreateRequest;
import com.example.msaccount.model.request.admin.AccountUpdateRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeycloakUserService {

    private final KeycloakProvider keycloakProvider;
    private final KeycloakMapper keycloakMapper;

    @Value("${KEYCLOAK_REALM}")
    private String realm;

    @Value("${KEYCLOAK_CLIENT_ID}")
    private String clientId;

    @Value("${KEYCLOAK_TOKEN_URI}")
    private String tokenUri;

    @Value("${KEYCLOAK_CLIENT_SECRET}")
    private String clientSecret;

    private UsersResource getUsersResource() {
        return keycloakProvider.getInstance().realm(realm).users();
    }

    private ClientResource getClientResource(String clientUUID) {
        return keycloakProvider.getInstance()
            .realm(realm)
            .clients()
            .get(clientUUID);
    }

    public Keycloak getKeycloakInstanceWithCredentials(String username, String password) {
        return keycloakProvider.newKeycloakBuilderWithPasswordCredentials(username, password)
            .build();
    }

    public void disableUserWithId(String accountId) {
        UserResource userResource = getUsersResource().get(accountId);
        UserRepresentation userRepresentation = userResource.toRepresentation();
        userRepresentation.setEnabled(false);
        userResource.update(userRepresentation);
    }

    public String refreshAccessToken(String refreshToken) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(tokenUri);

            // Request parameters
            List<BasicNameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("grant_type", "refresh_token"));
            params.add(new BasicNameValuePair("client_id", clientId));
            params.add(new BasicNameValuePair("refresh_token", refreshToken));

            // Include client_secret if necessary
            if (clientSecret != null && !clientSecret.isEmpty()) {
                params.add(new BasicNameValuePair("client_secret", clientSecret));
            }

            post.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");

            // Execute the request
            try (CloseableHttpResponse response = httpClient.execute(post)) {
                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode == 200) {
                    String jsonResponse = EntityUtils.toString(response.getEntity());
                    JsonNode tokenJson = new ObjectMapper().readTree(jsonResponse);

                    if (tokenJson.has("access_token")) {
                        return tokenJson.get("access_token").asText();
                    } else {
                        throw new RuntimeException("Invalid response: Missing access token");
                    }
                } else {
                    throw new RuntimeException("Failed to refresh token: " + statusCode);
                }
            }
        }
    }

    public KeycloakUserDTO fetchUserDetailsFromKeycloak(String accountId) {
        try {
            // get UsersResource from KeycloakProvider
            UsersResource usersResource = keycloakProvider.getInstance().realm(realm).users();

            UserRepresentation user = usersResource.get(accountId).toRepresentation();
            String clientUUID = getClientUUID();

            List<String> roles = usersResource.get(accountId)
                .roles()
                .clientLevel(clientUUID)
                .listAll()
                .stream()
                .map(RoleRepresentation::getName)
                .toList();

            return KeycloakUserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .roleNames(List.copyOf(roles))
                .build();

        } catch (NotFoundException e) {
            log.error("User with ID {} not found in Keycloak", accountId, e);
            throw new KeycloakResourceNotFoundException("User not found in Keycloak for ID: " + accountId);
        } catch (NotAuthorizedException e) {
            log.error("Unauthorized access while fetching user details from Keycloak for ID: {}", accountId, e);
            throw new UnauthorizedActionException("Unauthorized access to Keycloak for user ID: " + accountId);
        } catch (Exception e) {
            log.error("Unexpected error fetching user details from Keycloak for ID: {}", accountId, e);
            throw new RuntimeException("Error fetching user details from Keycloak for ID: " + accountId, e);
        }
    }



    private String getClientUUID() {
        return getClientsResource().findByClientId(clientId).get(0).getId();
    }

    private ClientsResource getClientsResource() {
        return keycloakProvider.getInstance().realm(realm).clients();
    }

    private RolesResource getClientRolesResource() {
        String clientUUID = getClientUUID();
        return getClientsResource().get(clientUUID).roles();
    }

    private List<RoleRepresentation> filterValidClientRoles(RolesResource rolesResource, List<String> roleNames) {
        List<RoleRepresentation> validRoles = new ArrayList<>();
        for (String roleName : roleNames) {
            RoleRepresentation roleRep = getClientRoleRepresentation(rolesResource, roleName);
            if (roleRep != null) {
                validRoles.add(roleRep);
            }
        }
        return validRoles;
    }

    private RoleRepresentation getClientRoleRepresentation(RolesResource clientRolesResource, String roleName) {
        try {
            RoleResource roleResource = clientRolesResource.get(roleName);
            RoleRepresentation roleRep = roleResource.toRepresentation();
            if (roleRep == null) {
                log.warn("Role '{}' does not exist in client '{}'", roleName, clientId);
                return null;
            }
            return roleRep;
        } catch (NotFoundException e) {
            log.warn("Role '{}' not found in client '{}'. Skipping this role.", roleName, clientId);
            return null;
        } catch (Exception e) {
            log.error("Error retrieving role '{}' from client '{}': {}", roleName, clientId, e.getMessage(), e);
            return null;
        }
    }

    public List<String> retrieveRoleNamesById(String uuid) {
        try {
            UserResource userResource = getUsersResource().get(uuid);

            UserRepresentation userRepresentation = userResource.toRepresentation();
            if (userRepresentation == null) {
                throw new KeycloakResourceNotFoundException("User not found with UUID: " + uuid);
            }

            List<RoleRepresentation> clientRoles = getClientRolesResource().list()
                .stream()
                .toList();
//                .orElseThrow(() -> new KeycloakClientNotFoundException("Client not found with id: " + clientId));

            return clientRoles.stream()
                .map(RoleRepresentation::getName)
                .toList();

        } catch (NotFoundException ex) {
            throw new KeycloakResourceNotFoundException("Resource not found: " + ex.getMessage());
        } catch (Exception ex) {
            throw new KeycloakRoleRetrievalException("Error retrieving roles for user: " +  ex.getMessage());
        }
    }

    public String retrieveUsernameByID(String uuid) {
        try {
            UserResource userResource = getUsersResource().get(uuid);
            UserRepresentation userRepresentation = userResource.toRepresentation();
            return userRepresentation.getUsername();
        } catch (NotFoundException ex) {
            log.info("Cannot found Keycloak user with id: " + uuid);
            throw new KeycloakResourceNotFoundException("Cannot found Keycloak user with id: " + uuid);
        }
    }


    @Transactional
    public KeycloakUserDTO createUser(AccountCreateRequest request) {
        UsersResource usersResource = getUsersResource();
        UserRepresentation kcUser = buildUserRepresentation(request);

        Response response = usersResource.create(kcUser);
        checkResponse(response);

        String userUUID = getUserUUIDFromResponse(response);

        List<RoleRepresentation> roleRep = assignClientRolesToUser(userUUID, request.roleNames());
        List<String> roleNamesAssigned = roleRep.stream()
            .map(RoleRepresentation::getName)
            .toList();

        return keycloakMapper.toKeycloakUserDTO(kcUser, roleNamesAssigned, userUUID);
    }

    public boolean isAdminUser(String uuid) {
        UserResource userResource = getUsersResource().get(uuid);

        Set<String> roleNames = userResource.roles().clientLevel(getClientUUID()).listAll().stream()
            .map(RoleRepresentation::getName)
            .collect(Collectors.toSet());
        return roleNames.contains("Admin");
    }


    private List<RoleRepresentation> assignClientRolesToUser(String userId, List<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty())
            return null;

        UsersResource usersResource = getUsersResource();
        UserResource userResource = usersResource.get(userId);

        try {
            // Retrieve or cache the client UUID
            String clientUUID = getClientUUID();
            RolesResource clientRolesResource = getClientRolesResource();

            // Fetch all RoleRepresentations for the specified role names
            List<RoleRepresentation> rolesToAssign = roleNames.stream()
                .map(roleName -> getClientRoleRepresentation(clientRolesResource, roleName))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

            // Identify missing roles
            List<String> missingRoles = roleNames.stream()
                .filter(roleName -> rolesToAssign.stream()
                    .noneMatch(role -> role.getName().equals(roleName))
                )
                .collect(Collectors.toList());

            if (!missingRoles.isEmpty()) {
                log.warn("Roles not found in client '{}': {}", clientId, missingRoles);
                throw new KeycloakRoleNotFoundException("Roles not found in client '" + clientId + "': " + missingRoles);
            }

            // Assign all roles in a single batch operation
            if (!rolesToAssign.isEmpty()) {
                userResource.roles().clientLevel(clientUUID).add(rolesToAssign);
                log.info("Assigned roles {} to user {}", roleNames, userId);
            }

            return rolesToAssign;

        } catch (Exception e) {
            log.error("Unexpected error during role assignment: {}", e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred while assigning roles to the user.", e);
        }
    }

    public KeycloakUserDTO updateAccount(AccountUpdateRequest request, String accountId) {
        try {
            // Fetch the user resource from Keycloak
            UserResource userResource = getUsersResource().get(accountId);

            // Fetch existing user representation
            UserRepresentation existingUser = userResource.toRepresentation();
            if (existingUser == null) { // x
                log.error("User not found with ID: {}", accountId);
                throw new KeycloakResourceNotFoundException("User not found with ID: " + accountId);
            }

            updateUserAttributes(existingUser, request);

            RolesResource clientRolesResource = getClientRolesResource();
            List<RoleRepresentation> validClientRoles = filterValidClientRoles(clientRolesResource, request.roleNames());
            List<String> validRoleNames = getRoleNames(validClientRoles);
            updateUserRoles(existingUser, validRoleNames);

            // Send the update request to Keycloak
            userResource.update(existingUser);
            log.info("Successfully updated user with ID: {}", accountId);

            UserRepresentation updatedUser = userResource.toRepresentation();
            return keycloakMapper.toKeycloakUserDTO(updatedUser, validRoleNames, existingUser.getId());

        } catch (NotFoundException ex) {
            log.error("Cannot found Keycloak user with id: {}", accountId);
            throw new KeycloakUserUpdateException("Cannot found Keycloak user with id: " + accountId);
        } catch (BadRequestException ex) {
            String details = extractMessageFromKeycloakResponse(ex.getResponse());
            log.error("Failed to update Keycloak user with id: {}. Error message: {}", accountId, details);
            throw new KeycloakUserUpdateException("Bad request: " + details);
        }
    }

    private void updateUserAttributes(UserRepresentation user, AccountUpdateRequest request) {
        user.setUsername(request.username());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setEnabled(request.accountEnable());
    }

    private void updateUserRoles(UserRepresentation userRep, List<String> validRoleNames) {
        Map<String, List<String>> clientRolesMap = new HashMap<>();
        clientRolesMap.put(getClientUUID(), validRoleNames);
        userRep.setClientRoles(clientRolesMap);
    }

    private List<String> getRoleNames(List<RoleRepresentation> roleReps) {
        return roleReps.stream()
            .map(RoleRepresentation::getName)
            .collect(Collectors.toList());
    }


//    private void updateUserRoles(List<String> roleNames) {
//        UsersResource usersResource = getUsersResource();
//        UserResource userResource = usersResource.get(getClientUUID());
//
//        MappingsRepresentation roles = userResource.roles().getAll();
//
//        // Remove all existing client-level roles
//        List<RoleRepresentation> existingClientRoles = clientRolesResource.list();
//        if (!existingClientRoles.isEmpty()) {
//            existingClientRoles.forEach(role -> {
////                    clientRolesResource.deleteRole(role.getName());
//                    log.info("Removed existing client-level roles for client '{}'. Roles: {}", clientId, role.getName()); // test
//                }
//            );
//            log.info("Removed existing client-level roles for client '{}'. Roles: {}", clientId, existingClientRoles); // test
//        }
//
//        // filter valid role names
//        List<RoleRepresentation> validRoles = filterValidRoles(clientRolesResource, roleNames);
//
//        // Retrieve RoleRepresentations for all valid provided roles
//        List<RoleRepresentation> newClientRoles = new ArrayList<>(validRoles);
//
//        newClientRoles.forEach(role -> {
//            clientRolesResource.create(role);
//            log.info("Assigned new client-level roles '{}' to client '{}'", role.getName(), clientId);
//        });
//    }



//    private void assignClientRolesToUser(String userId, List<String> roleNames) {
//        UsersResource usersResource = getUsersResource();
//        ClientsResource clientsResource = keycloakProvider.getInstance().realm(realm).clients();
//
//        // Retrieve client by clientId
//        String clientUUID = clientsResource.findByClientId(clientId).get(0).getId();
//        RolesResource clientRoles = clientsResource.get(clientUUID).roles();
//
//        // Fetch client role by name
//        RoleRepresentation role = clientRoles.get(roleName).toRepresentation();
//        if (role == null) {
//            throw new KeycloakRoleNotFoundException("Client role not found: " + roleName);
//        }
//
//        // Assign client-level role to the user
//        usersResource.get(userId).roles().clientLevel(clientUUID).add(Collections.singletonList(role));
//    }


    private CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation credentials = new CredentialRepresentation();
        credentials.setTemporary(false);
        credentials.setType(CredentialRepresentation.PASSWORD);
        credentials.setValue(password);
        return credentials;
    }

    private UserRepresentation buildUserRepresentation(AccountCreateRequest request) {
        CredentialRepresentation credentials = createPasswordCredentials(request.password());

        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(request.accountName());
        kcUser.setFirstName(request.firstName());
        kcUser.setLastName(request.lastName());
        kcUser.setEmail(request.email());
        kcUser.setEnabled(request.accountEnable());
        kcUser.setEmailVerified(false);
        kcUser.setCredentials(Collections.singletonList(credentials));

        return kcUser;
    }

//    private void checkResponse(Response response) {
//        if (response.getStatus() != 201) {
//            String errorJson = response.readEntity(String.class); // read the entire response body and convert it into a String
//            throw new KeycloakUserCreationException(errorJson);
//        }
//    }

    private void checkResponse(Response response) {
        if (response.getStatus() != 201) {
            String errorJson = response.readEntity(String.class); // Read response body as String
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                // Parse JSON into JsonNode
                JsonNode rootNode = objectMapper.readTree(errorJson);

                // Extract the errorMessage field
                String errorMessage = rootNode.path("errorMessage")
                    .asText("Unknown error occurred");

                throw new KeycloakUserCreationException(errorMessage);
            } catch (IOException ex) {
                throw new KeycloakUserCreationException("Failed to parse error response from Keycloak");
            }
        }
    }

    private String getUserUUIDFromResponse(Response response) {
        URI location = response.getLocation();
        if (location == null) {
            throw new RuntimeException("User ID not found in Keycloak response");
        }
        String path = location.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    private String extractMessageFromKeycloakResponse(Response response) {
        String errorJson = "";
        ObjectMapper objectMapper = new ObjectMapper();
        String errorMessage = "Unknown error occurred";

        if (response != null && response.hasEntity()) {
            try {
                errorJson = response.readEntity(String.class);
                // Parse JSON vào JsonNode
                JsonNode rootNode = objectMapper.readTree(errorJson);

                // extract field: errorMessage
                if (rootNode.has("errorMessage")) {
                    errorMessage = rootNode.get("errorMessage").asText();
                } else {
                    errorMessage = rootNode.toString();
                }
            } catch (IOException e) {
                log.error("Failed to parse error response from Keycloak: {}", e.getMessage(), e);
                errorMessage = "Failed to parse error response from Keycloak.";
            }
        }

        return errorMessage;
    }


}
