package com.example.gateway.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/province")
public class ProvinceProxyController {

    private final WebClient webClient;

    public ProvinceProxyController() {
        this.webClient = WebClient.builder()
            .baseUrl("https://vapi.vnappmob.com")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    @GetMapping
    public Mono<ResponseEntity<String>> getProvinces() {
        return webClient.get()
            .uri("/api/province")
            .retrieve()
            .bodyToMono(String.class)
            .map(ResponseEntity::ok)
            .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }
}