//package com.example.gateway.security;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpCookie;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
//import org.springframework.security.oauth2.server.resource.web.server.authentication.ServerBearerTokenAuthenticationConverter;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.util.List;
//
///**
// * Custom Bearer Token Authentication Converter to extract the access token from a cookie.
// */
//public class CustomBearerTokenAuthenticationConverter extends ServerBearerTokenAuthenticationConverter {
//
//    private static final Logger log = LoggerFactory.getLogger(CustomBearerTokenAuthenticationConverter.class);
//    private final String cookieName;
//
//    /**
//     * Constructor to set the cookie name.
//     *
//     * @param cookieName Name of the cookie to extract the access token from.
//     */
//    public CustomBearerTokenAuthenticationConverter(String cookieName) {
//        this.cookieName = cookieName;
//    }
//
//    /**
//     * Attempts to extract the bearer token from the specified cookie.
//     * Falls back to default behavior (Authorization header) if the cookie is absent.
//     *
//     * @param exchange ServerWebExchange instance
//     * @return Mono of Authentication if a token is found, empty otherwise
//     */
//    @Override
//    public Mono<Authentication> convert(ServerWebExchange exchange) {
//        List<HttpCookie> cookies = exchange.getRequest().getCookies().get(cookieName);
//        if (cookies != null && !cookies.isEmpty()) {
//            String token = cookies.get(0).getValue();
//            if (!token.isEmpty()) {
//                log.info("Extracted access_token from cookie: {}", token);
//                return Mono.just(new BearerTokenAuthenticationToken(token));
//            }
//        }
//        log.warn("No access token found in cookies");
//        return Mono.empty(); // Không kiểm tra header Authorization
//    }
//}
