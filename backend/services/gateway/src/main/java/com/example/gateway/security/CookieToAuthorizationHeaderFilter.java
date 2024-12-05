//package com.example.gateway.security;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpCookie;
//import org.springframework.http.HttpHeaders;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.web.server.WebFilter;
//import org.springframework.web.server.WebFilterChain;
//import reactor.core.publisher.Mono;
//
//import java.util.List;
//
///**
// * Custom WebFilter to extract the access token from a cookie and set it in the Authorization header.
// */
//@Component
//public class CookieToAuthorizationHeaderFilter implements WebFilter {
//
//    private static final Logger log = LoggerFactory.getLogger(CookieToAuthorizationHeaderFilter.class);
//    private final String cookieName;
//
//    /**
//     * Constructor to set the cookie name.
//     */
//    public CookieToAuthorizationHeaderFilter() {
//        this.cookieName = "access_token"; // Set your cookie name here
//    }
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//        List<HttpCookie> cookies = exchange.getRequest().getCookies().get(cookieName);
//        if (cookies != null && !cookies.isEmpty()) {
//            String token = cookies.get(0).getValue();
//            if (!token.isEmpty()) {
//                log.debug("Extracted access_token from cookie: {}", token);
//                // Mutate the request to add the Authorization header
//                exchange.getRequest().mutate()
//                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
//                    .build();
//            }
//        }
//        return chain.filter(exchange);
//    }
//}
