package com.example.msaccount.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
            .requestMatchers(HttpMethod.POST, "/api/v1/accounts/auth/login").permitAll()
//            .requestMatchers("/api/v1/admin/**").hasRole("Admin")
            .anyRequest().authenticated()
        )
        .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
        .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(authorize -> authorize
//                .requestMatchers(HttpMethod.POST, "/").permitAll()
//                .requestMatchers(HttpMethod.POST, "/api/v1/accounts/auth/login").permitAll()
//                .anyRequest().authenticated()
//            )
//            .oauth2ResourceServer((oauth2) -> oauth2.jwt(jwt -> jwt
//                .jwtAuthenticationConverter(new CustomJwtAuthenticationConverter())
//            ))
//            .csrf(AbstractHttpConfigurer::disable);
//        return http.build();
//    }
}
