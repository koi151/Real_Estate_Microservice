package com.koi151.mspropertycategory.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/property-category")
public class PropertyCategoryController {

//    @GetMapping("/")
//    public ResponseEntity<?> createCategory () {
//
//        return
//    }

    @GetMapping("/")
    public String test () {
        return "Category tested";
    }

}
