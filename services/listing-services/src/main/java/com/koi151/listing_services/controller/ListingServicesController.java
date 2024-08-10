package com.koi151.listing_services.controller;

import com.koi151.listing_services.model.request.PostServiceCreateRequest;
import com.koi151.listing_services.model.response.ResponseData;
import com.koi151.listing_services.service.ListingServicesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/listing-services")
public class ListingServicesController {

    private final ListingServicesService listingServicesService;

    @GetMapping("/")
    public ResponseEntity<String> findAllListingServices() {
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/")
    public ResponseEntity<ResponseData> createPostService(
        @RequestBody @Valid PostServiceCreateRequest request
    ) {
        var postService = listingServicesService.createPostService(request);
        return ResponseEntity.ok(ResponseData.builder()
            .desc("Created post service successfully" +
                (request.postServicePricing().startDate() == null
                    ? ", post service pricing start date by default will begin at current time" : ""))
            .data(postService)
            .build());
    }
}













