//package com.example.gateway.security;
//
//import org.springframework.http.HttpHeaders;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.web.server.WebFilter;
//import org.springframework.web.server.WebFilterChain;
//import reactor.core.publisher.Mono;
//
//@Component
//public class TokenRelayWebFilter implements WebFilter {
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//        return exchange.getPrincipal()
//            .cast(Authentication.class)
//            .filter(auth -> auth instanceof BearerTokenAuthenticationToken)
//            .cast(BearerTokenAuthenticationToken.class)
//            .map(BearerTokenAuthenticationToken::getToken)
//            .flatMap(token -> {
//                if (token != null && !token.isEmpty()) {
//                    ServerWebExchange mutatedExchange = exchange.mutate()
//                        .request(exchange.getRequest().mutate()
//                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
//                            .build())
//                        .build();
//                    return chain.filter(mutatedExchange);
//                }
//                return chain.filter(exchange);
//            })
//            .switchIfEmpty(chain.filter(exchange));
//    }
//}
