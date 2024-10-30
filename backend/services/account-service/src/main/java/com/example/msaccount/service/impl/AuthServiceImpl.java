package com.example.msaccount.service.impl;

import com.example.msaccount.customExceptions.InvalidAuthorizationHeaderException;
import com.example.msaccount.customExceptions.TokenParsingException;
import com.example.msaccount.model.dto.AccountDetailDTO;
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

import java.text.ParseException;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Value("${USER_CACHE_TTL}")
    private long userCacheTtl;

    @Value("${CACHE_REFRESH_THRESHOLD_PERCENT}")
    private int cacheRefreshThresholdPercent;

    private final KeycloakUserService kcUserService;
    private final RedisTemplate<String, Object> redisTemplate;


    @Override
    public AccessTokenResponse login(LoginRequest request) {
        AccessTokenResponse tokenResponse = kcUserService.getAccessToken(request.username(), request.password());
        AccountDetailDTO accDTO = extractUserInfoFromAccessToken(tokenResponse.getToken());
        cacheUserData(accDTO);
        cacheRefreshToken(tokenResponse.getRefreshToken(), accDTO.getAccountId(),
            Duration.ofSeconds(tokenResponse.getRefreshExpiresIn()));

        return tokenResponse;
    }

    private Set<String> extractRolesFromResourceAccess(JWTClaimsSet claims) {
        Map<String, Object> resourceAccess = (Map<String, Object>) claims.getClaim("resource_access");
        Set<String> allRoles = new HashSet<>();

        for (Map.Entry<String, Object> entry : resourceAccess.entrySet()) {
            if (entry.getValue() instanceof Map) {
                Map<String, Object> serviceAccess = (Map<String, Object>) entry.getValue();

                Object rolesObj = serviceAccess.get("roles");
                if (rolesObj instanceof List) {
                    List<String> roles = (List<String>) rolesObj;
                    allRoles.addAll(roles);
                }
            }
        }
        return allRoles;
    }

    public AccountDetailDTO extractUserInfoFromAccessToken(String accessToken) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(accessToken);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            String userId = claims.getSubject();
            Set<String> scopes = Arrays.stream(claims.getStringClaim("scope")
                    .split(" "))
                    .collect(Collectors.toSet());
            Set<String> roleNames = extractRolesFromResourceAccess(claims);

            return AccountDetailDTO.builder()
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

        } catch (ParseException ex) {
            throw new TokenParsingException("Failed to parse access token", ex);
        }
    }

    public String extractAccessTokenFromAuthHeader(String authorizationHeader) {
        return authorizationHeader.substring(7);
    }

    public String extractUserIDFromAuthHeader(String authorizationHeader) {
        String accessToken = extractAccessTokenFromAuthHeader(authorizationHeader);
        JWTClaimsSet claims = getJWTClaimsSet(accessToken);
        return claims.getSubject();
    }

    public JWTClaimsSet getJWTClaimsSet(String accessToken) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(accessToken);
            return signedJWT.getJWTClaimsSet();
        } catch (ParseException ex) {
            throw new TokenParsingException("Failed to parse access token", ex);
        }
    }



    private void cacheUserData(AccountDetailDTO accDTO) {
        String key = "userId:" + accDTO.getAccountId();
        redisTemplate.opsForValue().set(key, accDTO, Duration.ofSeconds(userCacheTtl));
    }

    private void cacheRefreshToken(String refreshToken, String userId, Duration ttl) {
        String key = "refresh_token:" + refreshToken;
        redisTemplate.opsForValue().set(key, userId, ttl);
    }

    public static String extractUserIdFromToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring(7);
            try {
                SignedJWT signedJWT = SignedJWT.parse(accessToken);
                JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
                return claims.getSubject();

            } catch (ParseException ex) {
                throw new TokenParsingException("Failed to parse access token", ex);
            }
        }
        throw new InvalidAuthorizationHeaderException("Invalid Authorization header");
    }
}
