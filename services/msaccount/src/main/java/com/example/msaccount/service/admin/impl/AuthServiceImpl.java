package com.example.msaccount.service.admin.impl;

import com.example.msaccount.model.dto.UserCache;
import com.example.msaccount.model.request.LoginRequest;
import com.example.msaccount.service.AuthService;
import com.example.msaccount.service.KeycloakUserService;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Value("${KEYCLOAK_REALM}")
    private String realm;

    @Value("${KEYCLOAK_SERVER_URL}")
    public String serverURL;

    private final KeycloakUserService kcUserService;
    private final RedisTemplate<String, Object> redisTemplate;


    @Override
    public AccessTokenResponse login(LoginRequest request) {
        AccessTokenResponse tokenResponse = kcUserService.getAccessToken(request.username(), request.password());
        UserCache userCache = extractUserInfoFromAccessToken(tokenResponse.getToken());

        cacheUserData(userCache, tokenResponse.getToken(), Duration.ofSeconds(tokenResponse.getExpiresIn()));
        cacheRefreshToken(tokenResponse.getRefreshToken(), userCache.getAccountId(),
            Duration.ofSeconds(tokenResponse.getRefreshExpiresIn()));

        return tokenResponse;
    }

    private Set<String> extractRolesFromResourceAccess(JWTClaimsSet claims) {
        Map<String, Object> resourceAccess = (Map<String, Object>) claims.getClaim("resource_access");
        Set<String> allRoles = new HashSet<>();

        for (Map.Entry<String, Object> entry : resourceAccess.entrySet()) {
            if (entry.getValue() instanceof Map) {
                Map<String, Object> serviceAccess = (Map<String, Object>) entry.getValue();

                // Check if the roles field exists and is a List
                Object rolesObj = serviceAccess.get("roles");
                if (rolesObj instanceof List) {
                    List<String> roles = (List<String>) rolesObj;
                    allRoles.addAll(roles);
                }
            }
        }
        return allRoles;
    }

    private UserCache extractUserInfoFromAccessToken(String accessToken) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(accessToken);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            String userId = claims.getSubject();
            Set<String> scopes = Arrays.stream(claims.getStringClaim("scope")
                    .split(" "))
                    .collect(Collectors.toSet());
            Set<String> roleNames = extractRolesFromResourceAccess(claims);

            return UserCache.builder()
                .accountId(userId)
                .username(claims.getStringClaim("preferred_username"))
                .firstName(claims.getStringClaim("firstName"))
                .lastName(claims.getStringClaim("lastName"))
                .roleNames(roleNames)
                .email(claims.getStringClaim("email"))
                .emailVerified(claims.getBooleanClaim("email_verified"))
                .scopes(scopes)
                .isAdmin(kcUserService.isAdminUser(userId))
                .build();

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse access token", e);
        }
    }

    private void cacheUserData(UserCache userCache, String accessToken, Duration ttl) {
        String key = "access_token:" + accessToken;
        redisTemplate.opsForValue().set(key, userCache, ttl);
    }

    private void cacheRefreshToken(String refreshToken, String userId, Duration ttl) {
        String key = "refresh_token:" + refreshToken;
        redisTemplate.opsForValue().set(key, userId, ttl);
    }

    public static String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        throw new RuntimeException("Invalid Authorization header");
    }
}
