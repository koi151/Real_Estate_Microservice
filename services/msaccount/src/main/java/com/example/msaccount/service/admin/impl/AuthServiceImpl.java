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

    @Value("${KEYCLOAK_AUTH_SERVER_URL}")
    private String authServerUrl;

    private final KeycloakUserService kcUserService;
    private final RedisTemplate<String, Object> redisTemplate;


//    public String extractUserIdFromAccessToken(String accessToken) {
//        Claims claims = Jwts.parserBuilder()
//            .setSigningKey(getKeycloakPublicKey())
//            .build()
//            .parseClaimsJws(accessToken)
//            .getBody();
//
//        return claims.getSubject();
//    }

//    private PublicKey getKeycloakPublicKey() {
//        try {
//            String publicKeyPEM = getKeycloakPublicKey();
//            publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----", "")
//                .replace("-----END PUBLIC KEY-----", "")
//                .replaceAll("\\s", "");
//
//            byte[] publicKeyDER = Base64.getDecoder().decode(publicKeyPEM);
//
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//            return keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyDER));
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to get Keycloak public key", e);
//        }
//    }

    @Override
    public AccessTokenResponse login(LoginRequest request) {
        AccessTokenResponse tokenResponse = kcUserService.getAccessToken(request.username(), request.password());

        UserCache userCache = extractUserInfoFromAccessToken(tokenResponse.getToken());

        cacheUserData(userCache, tokenResponse.getToken(), Duration.ofSeconds(tokenResponse.getExpiresIn()));
        cacheRefreshToken(tokenResponse.getRefreshToken(), userCache.getUserId(),
            Duration.ofSeconds(tokenResponse.getRefreshExpiresIn()));

        return tokenResponse;
    }


    private UserCache extractUserInfoFromAccessToken(String accessToken) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(accessToken);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            String userId = claims.getSubject();
            String email = claims.getStringClaim("email"); // need to adjust Keycloak setup
            Set<String> scopes = Arrays.stream(claims.getStringClaim("scope")
                    .split(" "))
                    .collect(Collectors.toSet());

            return UserCache.builder()
                .userId(userId)
                .email(email)
                .scopes(scopes)
                .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse access token", e);
        }
    }

    private void cacheUserData(UserCache userCache, String accessToken, Duration ttl) {
        String key = "access_token:" + accessToken;
        redisTemplate.opsForValue().set(key, userCache, ttl);
    }

    public UserCache getCachedUserData(String accessToken) {
        String key = "user:token:" + accessToken;
        return (UserCache) redisTemplate.opsForValue().get(key);
    }

    private void cacheRefreshToken(String refreshToken, String userId, Duration ttl) {
        String key = "refresh_token:" + refreshToken;
        redisTemplate.opsForValue().set(key, userId, ttl);
    }

}
