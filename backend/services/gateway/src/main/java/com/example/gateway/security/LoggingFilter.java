//package com.example.gateway.security;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpCookie;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.WebFilter;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.web.server.WebFilterChain;
//import reactor.core.publisher.Mono;
//
//import java.util.List;
//
///**
// * Custom WebFilter to log incoming HTTP requests, headers, and cookies.
// */
//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
//public class LoggingFilter implements WebFilter {
//
//    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//        log.debug("Incoming Request: {} {}", exchange.getRequest().getMethod(), exchange.getRequest().getURI());
//
//        // Log headers
//        exchange.getRequest().getHeaders().forEach((key, values) -> {
//            log.debug("Header: {} = {}", key, String.join(",", values));
//        });
//
//        // Log cookies
//        List<HttpCookie> cookies = exchange.getRequest().getCookies().get("access_token");
//        if (cookies != null && !cookies.isEmpty()) {
//            log.debug("Cookie 'access_token' found: {}", cookies.get(0).getValue());
//        } else {
//            log.debug("Cookie 'access_token' not found.");
//        }
//
//        return chain.filter(exchange);
//    }
//}
