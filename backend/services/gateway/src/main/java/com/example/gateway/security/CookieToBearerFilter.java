package com.example.gateway.security;

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


@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@RequiredArgsConstructor
public class CookieToBearerFilter implements WebFilter {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        log.info("Path: {}", request.getPath());
        log.info("All Cookies: {}", request.getCookies());

        HttpCookie accessTokenCookie = request.getCookies().getFirst("access_token");
        log.info("Access Token Cookie: {}", accessTokenCookie);

        if (accessTokenCookie != null) {
            String accessToken = accessTokenCookie.getValue();
            log.info("Found access token in cookie");

            ServerHttpRequest modifiedRequest = request
                .mutate()
                .header("Authorization", "Bearer " + accessToken)
                .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        } else {
            String accessTokenRedis = (String) redisTemplate.opsForValue().get("access_token");
            if (accessTokenRedis != null) {
                log.info("Access Token Redis: {}", accessTokenRedis);

                ServerHttpRequest modifiedRequest = request
                    .mutate()
                    .header("Authorization", "Bearer " + accessTokenRedis)
                    .build();

                return chain.filter(exchange.mutate().request(modifiedRequest).build());
            }
            log.info("No access token cookie found");
        }

        return chain.filter(exchange);
    }
}
