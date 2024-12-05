package com.example.msaccount.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> authorize
//                .requestMatchers(HttpMethod.GET, "/api/v1/accounts/auth/callback").permitAll()
//                .requestMatchers(HttpMethod.GET, "/api/v1/accounts/auth/login").permitAll()
//                .requestMatchers(HttpMethod.POST, "/api/v1/accounts/auth/token").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/oauth/callback").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/accounts/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/accounts/auth/token").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
//            .exceptionHandling(exception -> {
//            });

        return http.build();
    }
}
