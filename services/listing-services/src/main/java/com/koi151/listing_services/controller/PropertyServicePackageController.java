package com.koi151.listing_services.controller;

import com.koi151.listing_services.model.request.PropertyServicePackageCreateRequest;
import com.koi151.listing_services.model.response.ResponseData;
import com.koi151.listing_services.service.ListingServicesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/listing-services")
public class PropertyServicePackageController {

    private final ListingServicesService listingServicesService;
    @PostMapping("/")
    public ResponseEntity<ResponseData> createPropertyServicePackage(@RequestBody @Valid PropertyServicePackageCreateRequest request) {
        var propertyServicePackage = listingServicesService.createPostServicePackage(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseData.builder()
            .data(propertyServicePackage)
            .desc("Property service package created successfully")
            .build());
    }
}




















