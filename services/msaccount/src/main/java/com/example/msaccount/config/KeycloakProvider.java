package com.example.msaccount.config;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@NoArgsConstructor
public class KeycloakProvider {

    @Value("${KEYCLOAK_SERVER_URL}")
    public String serverURL;

    @Value("${KEYCLOAK_REALM}")
    public String realm;

    @Value("${KEYCLOAK_CLIENT_ID}")
    public String clientID;

    @Value("${KEYCLOAK_CLIENT_SECRET}")
    public String clientSecret;

    private static final Keycloak keycloak = null;

    public Keycloak getInstance() {
        return KeycloakBuilder.builder()
            .realm(realm)
            .serverUrl(serverURL)
            .clientId(clientID)
            .clientSecret(clientSecret)
            .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
            .build();
    }

    public RealmResource getRealmResource() {
        Keycloak keycloak = getInstance();
        return keycloak.realm(realm);
    }


    public KeycloakBuilder newKeycloakBuilderWithPasswordCredentials(String username, String password) {
        return KeycloakBuilder.builder()
            .realm(realm)
            .serverUrl(serverURL)
            .clientId(clientID)
            .clientSecret(clientSecret)
            .username(username)
            .password(password);
    }

    public JsonNode refreshToken(String refreshToken) throws UnirestException {
        String url = serverURL + "/realms/" + realm + "/protocol/openid-connect/token";
        return Unirest.post(url)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .field("client_id", clientID)
            .field("client_secret", clientSecret)
            .field("refresh_token", refreshToken)
            .field("grant_type", "refresh_token")
            .asJson().getBody();
    }

}