package com.example.msaccount.service.admin.impl;

import org.keycloak.representations.AccessTokenResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class TokenStorageService {

    private final RedisTemplate<String, Object> redisTemplate;

    public TokenStorageService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void storeTokens(String userId, AccessTokenResponse accessTokenResponse) {
        // Store access token
        redisTemplate.opsForValue().set(
            "user:" + userId + ":access_token",
            accessTokenResponse.getToken(),
            accessTokenResponse.getExpiresIn(),
            TimeUnit.SECONDS
        );

        // Store refresh token
        redisTemplate.opsForValue().set(
            "user:" + userId + ":refresh_token",
            accessTokenResponse.getRefreshToken(),
            accessTokenResponse.getRefreshExpiresIn(),
            TimeUnit.SECONDS
        );
    }
}

