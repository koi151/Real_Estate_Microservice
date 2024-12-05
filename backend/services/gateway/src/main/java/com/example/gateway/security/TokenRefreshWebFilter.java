//package com.example.gateway.security;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.exceptions.JWTDecodeException;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpCookie;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseCookie;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.oauth2.client.*;
//import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
//import org.springframework.security.oauth2.core.OAuth2RefreshToken;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.web.server.WebFilter;
//import org.springframework.web.server.WebFilterChain;
//import reactor.core.publisher.Mono;
//
//import java.time.Duration;
//import java.time.Instant;
//
//@Slf4j
//public class TokenRefreshWebFilter implements WebFilter {
//    private static final String ACCESS_TOKEN_COOKIE_NAME = "access_token";
//    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
//    private static final Duration TOKEN_EXPIRY_THRESHOLD = Duration.ofMinutes(5);
//    private static final String TOKEN_HANDLING_IN_PROGRESS = "tokenHandlingInProgress";
//
//    private final ReactiveClientRegistrationRepository clientRegistrationRepository;
//    private final ReactiveOAuth2AuthorizedClientManager authorizedClientManager;
//
//    @Value("${KEYCLOAK_ACCESS_TOKEN_LIFESPAN}")
//    private long accessTokenLifespan;
//
//    public TokenRefreshWebFilter(
//        ReactiveClientRegistrationRepository clientRegistrationRepository,
//        ReactiveOAuth2AuthorizedClientManager authorizedClientManager,
//        long accessTokenLifespan) {
//        this.clientRegistrationRepository = clientRegistrationRepository;
//        this.authorizedClientManager = authorizedClientManager;
//        this.accessTokenLifespan = accessTokenLifespan;
//    }
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//        if (Boolean.TRUE.equals(exchange.getAttribute(TOKEN_HANDLING_IN_PROGRESS))) {
//            return chain.filter(exchange);
//        }
//
//        HttpCookie refreshTokenCookie = exchange.getRequest().getCookies().getFirst(REFRESH_TOKEN_COOKIE_NAME);
//        HttpCookie accessTokenCookie = exchange.getRequest().getCookies().getFirst(ACCESS_TOKEN_COOKIE_NAME);
//
//        if (refreshTokenCookie == null || accessTokenCookie == null) {
//            return chain.filter(exchange);
//        }
//
//        exchange.getAttributes().put(TOKEN_HANDLING_IN_PROGRESS, true);
//
//        return shouldRefreshToken(accessTokenCookie.getValue())
//            .flatMap(shouldRefresh -> {
//                if (shouldRefresh) {
//                    return refreshToken(exchange, refreshTokenCookie.getValue())
//                        .then(chain.filter(exchange));
//                }
//                return chain.filter(exchange);
//            })
//            .onErrorResume(error -> {
//                log.error("Token refresh failed in filter: {}", error.getMessage());
//                if (!exchange.getResponse().isCommitted()) {
//                    return clearTokenCookies(exchange)
//                        .then(Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)));
//                }
//                return Mono.error(error);
//            });
//    }
//
//    private Mono<Boolean> shouldRefreshToken(String accessToken) {
//        try {
//            DecodedJWT jwt = JWT.decode(accessToken);
//            Instant expiresAt = jwt.getExpiresAt().toInstant();
//            Instant now = Instant.now();
//            return Mono.just(expiresAt.isBefore(now.plus(TOKEN_EXPIRY_THRESHOLD)));
//        } catch (JWTDecodeException e) {
//            log.error("Failed to decode JWT: {}", e.getMessage());
//            return Mono.just(false);
//        }
//    }
//
//    private Mono<Void> refreshToken(ServerWebExchange exchange, String refreshToken) {
//        return createAuthorizedClient(refreshToken)
//            .flatMap(client -> updateCookies(exchange, client))
//            .onErrorResume(error -> {
//                log.error("Token refresh failed in refreshToken method: {}", error.getMessage());
//                if (!exchange.getResponse().isCommitted()) {
//                    return clearTokenCookies(exchange);
//                }
//                return Mono.error(error);
//            });
//    }
//
//    private Mono<OAuth2AuthorizedClient> createAuthorizedClient(String refreshToken) {
//        return clientRegistrationRepository.findByRegistrationId("real-estate-microservice")
//            .flatMap(registration -> {
//                if (registration == null) {
//                    log.error("Client registration not found for ID: real-estate-microservice");
//                    return Mono.empty();
//                }
//
//                OAuth2RefreshToken oAuth2RefreshToken = new OAuth2RefreshToken(
//                    refreshToken,
//                    Instant.now(),
//                    Instant.now().plus(Duration.ofSeconds(accessTokenLifespan))
//                );
//
//                OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest.withClientRegistrationId("real-estate-microservice")
//                    .principal(new UsernamePasswordAuthenticationToken("user", null))
//                    .attribute(OAuth2RefreshToken.class.getName(), oAuth2RefreshToken)
//                    .build();
//
//                return authorizedClientManager.authorize(request);
//            });
//    }
//
//    private Mono<Void> updateCookies(ServerWebExchange exchange, OAuth2AuthorizedClient client) {
//        if (client != null && client.getAccessToken() != null) {
//            if (!exchange.getResponse().isCommitted()) {
//                addCookie(exchange, ACCESS_TOKEN_COOKIE_NAME,
//                    client.getAccessToken().getTokenValue(),
//                    client.getAccessToken().getExpiresAt());
//
//                if (client.getRefreshToken() != null) {
//                    addCookie(exchange, REFRESH_TOKEN_COOKIE_NAME,
//                        client.getRefreshToken().getTokenValue(),
//                        client.getRefreshToken().getExpiresAt());
//                }
//            } else {
//                log.warn("Cannot update cookies because response is already committed");
//            }
//            return Mono.empty();
//        }
//        return clearTokenCookies(exchange);
//    }
//
//    private void addCookie(ServerWebExchange exchange, String name, String value, Instant expiresAt) {
//        ResponseCookie cookie = ResponseCookie.from(name, value)
//            .httpOnly(true)
//            .secure(true)
//            .path("/")
//            .maxAge(Duration.between(Instant.now(), expiresAt))
//            .build();
//        exchange.getResponse().addCookie(cookie);
//        log.info("Added/Updated cookie: {}", name);
//    }
//
//    private Mono<Void> clearTokenCookies(ServerWebExchange exchange) {
//        if (exchange.getResponse().isCommitted()) {
//            log.warn("Response has already been committed, cannot clear cookies");
//            return Mono.empty();
//        }
//
//        ResponseCookie accessCookie = ResponseCookie.from(ACCESS_TOKEN_COOKIE_NAME, "")
//            .maxAge(0)
//            .httpOnly(true)
//            .path("/")
//            .build();
//
//        ResponseCookie refreshCookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, "")
//            .maxAge(0)
//            .httpOnly(true)
//            .path("/")
//            .build();
//
//        exchange.getResponse().addCookie(accessCookie);
//        exchange.getResponse().addCookie(refreshCookie);
//        log.info("Cleared cookies: {}, {}", ACCESS_TOKEN_COOKIE_NAME, REFRESH_TOKEN_COOKIE_NAME);
//        return Mono.empty();
//    }
//}
