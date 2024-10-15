package com.example.msaccount.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication instanceof JwtAuthenticationToken jwtAuthToken) {
                String tokenValue = jwtAuthToken.getToken().getTokenValue();
                requestTemplate.header("Authorization", "Bearer " + tokenValue);
            } else if (authentication != null && authentication.getCredentials() instanceof Jwt jwt) {
                String tokenValue = jwt.getTokenValue();
                requestTemplate.header("Authorization", "Bearer " + tokenValue);
            } else {
                throw new IllegalStateException("Cannot obtain JWT token for authentication");
            }
        };
    }
}
