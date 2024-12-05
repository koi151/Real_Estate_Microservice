//package com.example.gateway.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.client.*;
//import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
//import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProvider;
//import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProviderBuilder;
//import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
//
//@Configuration
//public class AuthorizedClientManagerConfig {
//    @Bean
//    public ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
//        ReactiveClientRegistrationRepository clientRegistrationRepository,
//        ReactiveOAuth2AuthorizedClientService authorizedClientService) {
//
//        ReactiveOAuth2AuthorizedClientProvider authorizedClientProvider =
//            ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
//                .refreshToken()
//                .build();
//
//        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager manager =
//            new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
//                clientRegistrationRepository, authorizedClientService);
//        manager.setAuthorizedClientProvider(authorizedClientProvider);
//
//        return manager;
//    }
//}