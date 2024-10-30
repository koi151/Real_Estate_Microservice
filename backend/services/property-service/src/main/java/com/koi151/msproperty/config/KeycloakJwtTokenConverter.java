//package com.koi151.msproperties.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.lang.NonNull;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.jwt.JwtClaimNames;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
//import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
//
//import java.util.*;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//@RequiredArgsConstructor
//public class KeycloakJwtTokenConverter implements Converter<Jwt, JwtAuthenticationToken> {
//
//    private static final String KEYCLOAK_REALM_ACCESS = "realm_access";
//    private static final String KEYCLOAK_RESOURCE_ACCESS = "resource_access";
//    private static final String KEYCLOAK_ROLES = "roles";
//    private static final String KEYCLOAK_ROLE_PREFIX = "ROLE_";
//    private static final String RESOURCE_ID = "properties-service";
//    private static final String PRINCIPAL_ATTR = "preferred_username";
//
//    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter;
//
//    @Override
//    public JwtAuthenticationToken convert(@NonNull Jwt jwt) {
//        // Extract roles from 'resource_access' claim
//        Stream<GrantedAuthority> accesses = Optional.of(jwt)
//                .map(token -> token.getClaimAsMap(KEYCLOAK_RESOURCE_ACCESS))
//                .map(claimMap -> (Map<String, Object>) claimMap.get(RESOURCE_ID))
//                .map(resourceData -> (Collection<String>) resourceData.get(KEYCLOAK_ROLES))
//                .stream()
//                .flatMap(Collection::stream) // Flatten the collection
//                .map(role -> new SimpleGrantedAuthority(KEYCLOAK_ROLE_PREFIX + role));
//
//        // Extract roles from 'realm_access' claim
//        Stream<GrantedAuthority> realm = Optional.of(jwt)
//                .map(token -> token.getClaimAsMap(KEYCLOAK_REALM_ACCESS))
//                .map(resourceData -> (Collection<String>) resourceData.get(KEYCLOAK_ROLES))
//                .stream()
//                .flatMap(Collection::stream) // Flatten the collection
//                .map(role -> new SimpleGrantedAuthority(KEYCLOAK_ROLE_PREFIX + role));
//
//        // Combine roles from both claims and those from the default converter
//        Set<GrantedAuthority> authorities = Stream.concat(
//                Stream.concat(jwtGrantedAuthoritiesConverter.convert(jwt).stream(), accesses),
//                realm
//        ).collect(Collectors.toSet());
//
//        String principalClaimName = jwt.getClaimAsString(PRINCIPAL_ATTR);
//        if (principalClaimName == null) {
//            principalClaimName = jwt.getClaimAsString(JwtClaimNames.SUB);
//        }
//
//        return new JwtAuthenticationToken(jwt, authorities, principalClaimName);
//    }
//
//    // an example
//
////        "sub" -> "dcbfb403-75cf-4388-8c6a-97d642964239"
////        "resource_access" -> {LinkedTreeMap@8450}  size = 1
////        "email_verified" -> {Boolean@8452} false
////        "iss" -> "http://localhost:8080/auth/realms/SpringBootKeycloak"
////        "typ" -> "Bearer"
////        "preferred_username" -> "user1"
////        "sid" -> "ff8bfcdd-1d91-4773-9c84-9b0a12dcd5a2"
////        "aud" -> {ArrayList@8462}  size = 1
////        "acr" -> "1"
////        "realm_access" -> {LinkedTreeMap@8466}  size = 1
////        "azp" -> "login-app"
////        "scope" -> "email profile"
////        "exp" -> {Instant@8426} "2024-01-28T15:04:17Z"
////        "session_state" -> "ff8bfcdd-1d91-4773-9c84-9b0a12dcd5a2"
////        "iat" -> {Instant@8425} "2024-01-28T14:59:17Z"
////        "jti" -> "1d73943b-cc38-4a8d-989f-f120caa59ce0"
//
//}