//package com.example.gateway.security;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.web.server.context.ServerSecurityContextRepository;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//@Component
//@Slf4j
//public class CustomSecurityContextRepository implements ServerSecurityContextRepository {
//
//    @Override
//    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
//        log.info("Saving SecurityContext: {}", context);
//        return Mono.empty();
//    }
//
//    @Override
//    public Mono<SecurityContext> load(ServerWebExchange exchange) {
//        return Mono.empty();
//    }
//}
