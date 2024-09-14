package com.example.msaccount.service;

import com.example.msaccount.config.KeycloakProvider;
import com.example.msaccount.customExceptions.KeycloakAccountCreationException;
import com.example.msaccount.model.request.AccountCreateRequest;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class KeycloakAdminClientService {

    @Value("${KEYCLOAK_REALM}")
    public String realm;

    private final KeycloakProvider kcProvider;

    public void createKeycloakUser(AccountCreateRequest request, String avatarUrl) {
        UsersResource usersResource = kcProvider.getInstance().realm(realm).users();
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(request.password());

        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(request.accountName());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setFirstName(request.firstName());
        kcUser.setLastName(request.lastName());
        kcUser.setEmail(request.email());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);


        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("avatarUrl", Collections.singletonList(avatarUrl));
        attributes.put("phone", Collections.singletonList(request.phone()));

        kcUser.setAttributes(attributes);


        keyCloakResponseCheck(usersResource.create(kcUser));
    }

    public void keyCloakResponseCheck(Response response) {
        if (response.getStatus() != 201)
            throw new KeycloakAccountCreationException("Keycloak failed to create account, recheck again");
    }

    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();

        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);

        return passwordCredentials;
    }
}