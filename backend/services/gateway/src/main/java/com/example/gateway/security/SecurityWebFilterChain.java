//package com.example.gateway.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
//
//@Bean
//public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//    // Tạo bộ lọc tùy chỉnh cho Bearer Token
//    AuthenticationWebFilter customBearerTokenFilter = customBearerAuthenticationWebFilter();
//
//
//
//    return http
//        .csrf(ServerHttpSecurity.CsrfSpec::disable)
//        .authorizeExchange(exchanges -> exchanges
//            .pathMatchers("/eureka/**").permitAll()
//            .pathMatchers("/actuator/**").permitAll()
//            .pathMatchers(HttpMethod.GET, "/api/v1/accounts/auth/**").permitAll()
//            .anyExchange().authenticated()
//        )
//        .addFilterAt(customBearerTokenFilter, SecurityWebFiltersOrder.AUTHENTICATION) // Thêm bộ lọc tùy chỉnh
//        .oauth2ResourceServer(oauth2 -> oauth2
//            .jwt(jwt -> jwt
//                .jwkSetUri(jwkSetUri)
//                .jwtAuthenticationConverter(jwtAuthenticationConverter())
//            )
//        )
//        .exceptionHandling(exceptionHandling -> exceptionHandling
//            .authenticationEntryPoint((exchange, ex) -> {
//                log.error("Authentication failed: {}", ex.getMessage(), ex);
//                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//                return exchange.getResponse().setComplete();
//            })
//            .accessDeniedHandler((exchange, denied) -> {
//                log.error("Access denied: {}", denied.getMessage(), denied);
//                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
//                return exchange.getResponse().setComplete();
//            })
//        )
//        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//        .build();
//}
