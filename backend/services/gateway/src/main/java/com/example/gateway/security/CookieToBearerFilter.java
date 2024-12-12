package com.example.gateway.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.gateway.TokenService.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Date;


@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@RequiredArgsConstructor
public class CookieToBearerFilter implements WebFilter {

    private final RedisTemplate<String, Object> redisTemplate;
    private final TokenService tokenService; // Service to handle token exchange with OIDC provider

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        log.info("Path: {}", request.getPath());
        log.info("All Cookies: {}", request.getCookies());

        HttpCookie accessTokenCookie = request.getCookies().getFirst("access_token");
        log.info("Access Token Cookie: {}", accessTokenCookie);

        if (accessTokenCookie != null) {
            String accessToken = accessTokenCookie.getValue();
            if (isTokenValid(accessToken)) {
                log.info("Found valid access token in cookie");

                ServerHttpRequest modifiedRequest = request
                    .mutate()
                    .header("Authorization", "Bearer " + accessToken)
                    .build();

                return chain.filter(exchange.mutate().request(modifiedRequest).build());
            } else {
                log.info("Access token is invalid or expired");
            }
        }

        // Retrieve access token from Redis
        String accessTokenRedis = (String) redisTemplate.opsForValue().get("access_token");
        if (accessTokenRedis != null && isTokenValid(accessTokenRedis)) {
            log.info("Found valid access token in Redis");

            ServerHttpRequest modifiedRequest = request
                .mutate()
                .header("Authorization", "Bearer " + accessTokenRedis)
                .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        }

        // If access token is not found or is expired, attempt to refresh it
        String refreshToken = (String) redisTemplate.opsForValue().get("refresh_token");
        if (refreshToken != null) {
            log.info("Attempting to refresh access token using refresh token");
            return tokenService.refreshAccessToken(refreshToken)
                .flatMap(newAccessToken -> {
                    // save new access token in redis
                    redisTemplate.opsForValue().set("access_token", newAccessToken, Duration.ofMinutes(5));
                    log.info("New access token saved in Redis");

                    // add new access token to the request header
                    ServerHttpRequest modifiedRequest = request
                        .mutate()
                        .header("Authorization", "Bearer " + newAccessToken)
                        .build();

                    return chain.filter(exchange.mutate().request(modifiedRequest).build());
                })
                .onErrorResume(e -> {
                    log.error("Failed to refresh access token: " + e);
                    return chain.filter(exchange); // Proceed without an access token
                });
        }

        log.info("No access token or refresh token found");
        return chain.filter(exchange);
    }

    private boolean isTokenValid(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            return decodedJWT.getExpiresAt().after(new Date());
        } catch (Exception e) {
            log.error("Invalid token", e);
            return false;
        }
    }
}
