package com.example.gateway.securiity;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@EnableMethodSecurity
@Slf4j
public class SecurityConfig {

    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchange -> exchange
                .pathMatchers("(/eureka/**").permitAll()
                .pathMatchers(HttpMethod.POST,"api/v1/accounts/auth/login").permitAll()
                .anyExchange()
                .authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint((exchange, ex) -> {
                    log.error("Authentication error: ", ex);
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return Mono.empty();
                })
                .accessDeniedHandler((exchange, ex) -> {
                    log.error("Access denied: ", ex);
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return Mono.empty();
                })
            );

        return serverHttpSecurity.build();
    }



//    @Bean
//    public Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverterReactive() {
//        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
//        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverterForKeycloak());
//
//        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
//    }

//    @Bean
//    public Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverterForKeycloak() {
//        return jwt -> {
//            Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
//
//            Object client = resourceAccess.get("properties-service");
//
//            if (client == null)
//                return new ArrayList<>();
//
//            LinkedTreeMap<String, List<String>> clientRoleMap = (LinkedTreeMap<String, List<String>>) client;
//            List<String> clientRoles = clientRoleMap.get("roles");
//
//            return clientRoles != null ? clientRoles.stream()
//                    .map(SimpleGrantedAuthority::new)
//                    .collect(Collectors.toList()) : new ArrayList<>();
//        };
//    }

}