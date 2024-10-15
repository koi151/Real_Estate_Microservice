package com.koi151.listing_services.controller;

import com.koi151.listing_services.model.request.PostServiceCategoryCreateRequest;
import com.koi151.listing_services.model.response.ResponseData;
import com.koi151.listing_services.service.PostServiceCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/listing-services/categories")
public class PostServiceCategoryController {

    private final PostServiceCategoryService postServiceCategoryService;

    @GetMapping("/")
    public ResponseEntity<String> findAllPostServiceCategories() {
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/")
    public ResponseEntity<ResponseData> createPostServiceCategory(@RequestBody @Valid PostServiceCategoryCreateRequest request) {
        var category = postServiceCategoryService.createPostServiceCategory(request);
        return ResponseEntity.ok(
            ResponseData.builder()
                .desc("Post service category created successfully")
                .data(category)
                .build()
        );
    }
}










