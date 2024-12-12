package com.example.gateway.security;

import com.example.gateway.TokenService.TokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final RedisTemplate<String, Object> redisTemplate;
    private final TokenService tokenService;

    public SecurityConfig(RedisTemplate<String, Object> redisTemplate, TokenService tokenService) {
        this.redisTemplate = redisTemplate;
        this.tokenService = tokenService;
    }
    @Bean
    public CookieToBearerFilter cookieToBearerFilter() {
        return new CookieToBearerFilter(redisTemplate, tokenService);
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchange -> exchange
                .pathMatchers("/eureka/**").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/v1/oauth/callback").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/v1/accounts/auth/login").permitAll()
                .pathMatchers(HttpMethod.POST, "/api/v1/accounts/auth/token").permitAll()
                .pathMatchers(HttpMethod.GET, "https://vapi.vnappmob.com/api/province/").permitAll()
                .pathMatchers("/api/v1/province/**").permitAll()
                .anyExchange().authenticated()
            )
            .addFilterAt(cookieToBearerFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint((exchange, ex) -> {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return Mono.empty();
                })
                .accessDeniedHandler((exchange, ex) -> {
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return Mono.empty();
                })
            )
            .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3001"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "Accept",
            "Access-Control-Allow-Credentials",
            "Access-Control-Allow-Origin",
            "Origin"
        ));
        configuration.setAllowedHeaders(Collections.singletonList("*"));

        configuration.setExposedHeaders(Arrays.asList(
            "Authorization",
            "Set-Cookie",
            "Access-Control-Allow-Credentials",
            "Access-Control-Allow-Origin"
        ));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L); // Cache preflight request

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
