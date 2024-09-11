package com.koi151.msproperties.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // Initialize in a constructor or with a setter (not directly in the field)
//    private final KeycloakJwtTokenConverter keycloakJwtTokenConverter = new KeycloakJwtTokenConverter(new JwtGrantedAuthoritiesConverter());


//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
//        serverHttpSecurity
//            .csrf(ServerHttpSecurity.CsrfSpec::disable)
//            .authorizeExchange(exchange -> exchange
//                .pathMatchers("(/eureka/**")
//                .permitAll()
//                .anyExchange()
//                .authenticated()
//            )
//            .oauth2ResourceServer(oauth2 -> oauth2.jwt(keycloakJwtTokenConverter);
//        return serverHttpSecurity.build();
//    }

//    @Bean
//    public SecurityFilterChain myServerFilterChain(HttpSecurity http) throws Exception {
//        http
//            .authorizeHttpRequests(authorizeHttpRequests ->
//                authorizeHttpRequests
//                    .requestMatchers("/private/**")
//                    .authenticated()
//                    .requestMatchers("/**")
//                    .permitAll()
//            )
//            .oauth2ResourceServer(oauth2 ->
//                oauth2.jwt(jwt ->
//                    jwt.oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
//                )
//            );
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }
}