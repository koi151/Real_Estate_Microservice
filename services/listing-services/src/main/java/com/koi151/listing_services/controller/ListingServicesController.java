package com.koi151.listing_services.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/listing-services")
public class ListingServicesController {

    @GetMapping("/")
    public ResponseEntity<String> findAllListingServices() {
        return ResponseEntity.ok("OK");
    }
}
