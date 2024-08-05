package com.koi151.listing_services.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/listing-services/categories")
public class PostServiceCategoryController {

    @GetMapping("/")
    public ResponseEntity<String> findAllPostServiceCategories() {
        return ResponseEntity.ok("ok");
    }

}
