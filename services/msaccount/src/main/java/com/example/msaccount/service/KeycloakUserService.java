package com.example.msaccount.service;

import com.example.msaccount.config.KeycloakProvider;
import com.example.msaccount.customExceptions.KeycloakGroupNotFoundException;
import com.example.msaccount.customExceptions.KeycloakRoleNotFoundException;
import com.example.msaccount.customExceptions.KeycloakUserCreationException;
import com.example.msaccount.model.request.admin.AccountCreateRequest;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.GroupsResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakUserService {

    private final KeycloakProvider keycloakProvider;

    @Value("${KEYCLOAK_REALM}")
    private String realm;

    @Value("${KEYCLOAK_CLIENT_ID}")
    private String clientId;

    public Response createUser(AccountCreateRequest request) {
        UsersResource usersResource = getUsersResource();
        UserRepresentation kcUser = buildUserRepresentation(request);

        Response response = usersResource.create(kcUser);
        checkResponse(response);

        String userId = getUserIdFromResponse(response);

        assignClientRoleToUser(userId, request.roleName());
        assignGroupToUser(userId, getUserGroup(request.isAdmin()));

        return response;
    }

    public String getUserGroup(boolean isAdmin) {
        return isAdmin ? "Admin accounts" : "Client accounts";
    }

    private UsersResource getUsersResource() {
        return keycloakProvider.getInstance().realm(realm).users();
    }

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

    private void checkResponse(Response response) {
        if (response.getStatus() != 201) {
            throw new KeycloakUserCreationException("Failed to create user");
        }
    }

    private String getUserIdFromResponse(Response response) {
        URI location = response.getLocation();
        if (location == null) {
            throw new RuntimeException("User ID not found in response");
        }
        String path = location.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    private void assignClientRoleToUser(String userId, String roleName) {
        UsersResource usersResource = getUsersResource();
        ClientsResource clientsResource = keycloakProvider.getInstance().realm(realm).clients();

        // Retrieve client by clientId
        String clientUUID = clientsResource.findByClientId(clientId).get(0).getId();
        RolesResource clientRoles = clientsResource.get(clientUUID).roles();

        // Fetch client role by name
        RoleRepresentation role = clientRoles.get(roleName).toRepresentation();
        if (role == null) {
            throw new KeycloakRoleNotFoundException("Client role not found: " + roleName);
        }

        // Assign client-level role to the user
        usersResource.get(userId).roles().clientLevel(clientUUID).add(Collections.singletonList(role));
    }

    private void assignGroupToUser(String userId, String groupName) {
        UsersResource usersResource = getUsersResource();
        GroupsResource groupsResource = keycloakProvider.getInstance().realm(realm).groups();

        // Retrieve the group by name
        List<GroupRepresentation> groups = groupsResource.groups(groupName, 0, 1);
        if (groups.isEmpty()) {
            throw new KeycloakGroupNotFoundException("Cannot not found group in Keycloak with name: " + groupName);
        }

        // Get the group ID from the first group matching the name
        String groupId = groups.get(0).getId();

        // Assign group to the user
        usersResource.get(userId).joinGroup(groupId);
    }
}
