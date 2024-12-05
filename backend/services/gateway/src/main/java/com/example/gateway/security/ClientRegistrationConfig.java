//package com.example.gateway.security;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
//import org.springframework.security.oauth2.core.AuthorizationGrantType;
//
//@Configuration
//public class ClientRegistrationConfig {
//    @Value("${KEYCLOAK_CLIENT_ID}")
//    private String clientId;
//
//    @Value("${KEYCLOAK_CLIENT_SECRET}")
//    private String clientSecret;
//
//    @Value("${KEYCLOAK_REDIRECT_URI}")
//    private String clientRedirectUri;
//
//    @Value("${KEYCLOAK_AUTH_SERVER_URL}")
//    private String authServerUrl;
//
//    @Value("${KEYCLOAK_TOKEN_URI}")
//    private String tokenUri;
//
//    @Value("${KEYCLOAK_USER_INFO_URI}")
//    private String userInfoUri;
//
//    @Value("${KEYCLOAK_JWK_SET_URI}")
//    private String jwtSetUri;
//
//    @Value("${KEYCLOAK_ISSUER_URI}")
//    private String issuerUri;
//
//    @Bean
//    public ReactiveClientRegistrationRepository clientRegistrationRepository() {
//        return new InMemoryReactiveClientRegistrationRepository(keycloakClientRegistration());
//    }
//
//    private ClientRegistration keycloakClientRegistration() {
//        return ClientRegistration.withRegistrationId("real-estate-microservice")
//            .clientId(clientId)
//            .clientSecret(clientSecret)
//            .scope("openid", "profile", "email")
//            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//            .redirectUri(clientRedirectUri)
//            .authorizationUri(authServerUrl)
//            .tokenUri(tokenUri)
//            .userInfoUri(userInfoUri)
//            .jwkSetUri(jwtSetUri)
//            .issuerUri(issuerUri)
//            .build();
//    }
//}
