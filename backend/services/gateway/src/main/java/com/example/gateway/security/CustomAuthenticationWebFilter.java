//package com.example.gateway.security;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.AbstractAuthenticationToken;
//import org.springframework.security.authentication.ReactiveAuthenticationManager;
//import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
//import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
//import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
//import reactor.core.publisher.Mono;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.http.HttpCookie;
//
//@Slf4j
//public class CustomAuthenticationWebFilter extends AuthenticationWebFilter {
//
//    public CustomAuthenticationWebFilter(ReactiveAuthenticationManager authenticationManager) {
//        super(authenticationManager);
//        this.setServerAuthenticationConverter(new ServerAuthenticationConverter() {
//            @Override
//            public Mono<AbstractAuthenticationToken> convert(ServerWebExchange exchange) {
//                HttpCookie cookie = exchange.getRequest().getCookies().getFirst("access_token");
//                if (cookie != null) {
//                    String token = cookie.getValue();
//                    log.debug("Access token found in cookie: {}", token);
//                    return Mono.just(new BearerTokenAuthenticationToken(token));
//                }
//                log.debug("No access token found in cookies.");
//                return Mono.empty();
//            }
//        });
//    }
//}
