//package com.example.gateway.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
//import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
//import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
//
//@Configuration
//public class AuthorizedClientServiceConfig {
//
//    @Bean
//    public ReactiveOAuth2AuthorizedClientService authorizedClientService(
//        ReactiveClientRegistrationRepository clientRegistrationRepository) {
//        return new InMemoryReactiveOAuth2AuthorizedClientService(clientRegistrationRepository);
//    }
//}
////