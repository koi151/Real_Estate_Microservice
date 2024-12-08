package com.koi151.msproperty.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, LoggingFilter loggingFilter) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/actuator/health").permitAll()  // Allow unauthenticated access
                .requestMatchers(HttpMethod.GET, "/api/v1/province/**").permitAll()                .anyRequest().authenticated())
            .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
            .csrf(AbstractHttpConfigurer::disable)
            .addFilterBefore(loggingFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public LoggingFilter loggingFilter() {
        return new LoggingFilter();
    }

}
