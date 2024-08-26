package com.koi151.listing_services.controller;

import com.koi151.listing_services.model.request.PropertyServicePackageCreateRequest;
import com.koi151.listing_services.model.request.PropertyServicePackageSearchRequest;
import com.koi151.listing_services.model.response.ResponseData;
import com.koi151.listing_services.service.PropertyServicePackageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/listing-services/property-service-package")
public class PropertyServicePackageController {

    private final PropertyServicePackageService propertyServicePackageService;

    @GetMapping("/")
    public ResponseEntity<ResponseData> findPropertyServicePackageByCriteria(@RequestBody PropertyServicePackageSearchRequest request) {
        var result = propertyServicePackageService.findPropertyServicePackageByCriteria(request);

        String desc = (result != null ? "Successfully retrieved " : "No ") +
            "property service package found with " +
            (request.propertyId() != null
                ? "property id: " + request.propertyId()
                : "package id: " + request.packageId());

        return ResponseEntity.ok(
            ResponseData.builder()
                .data(result)
                .desc(desc)
                .build()
        );
    }

    @PostMapping("/")
    public ResponseEntity<ResponseData> createPropertyServicePackage(@RequestBody @Valid PropertyServicePackageCreateRequest request) {
        var propertyServicePackage = propertyServicePackageService.createPropertyServicePackage(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseData.builder()
            .data(propertyServicePackage)
            .desc("Property service package created successfully")
            .build());
    }
}




















