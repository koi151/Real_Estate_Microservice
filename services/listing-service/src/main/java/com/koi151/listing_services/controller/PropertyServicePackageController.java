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

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/listing-services/property-service-package")
public class PropertyServicePackageController {

    private final PropertyServicePackageService propertyServicePackageService;

    @GetMapping("/")
    public ResponseEntity<ResponseData> findPropertyServicePackageByCriteria(
            @RequestParam Map<String, String> params
    ) {
        var result = propertyServicePackageService.findPropertyServicePackageByCriteria(params);

        String desc = (result != null ? "Successfully retrieved " : "No ") +
            "property service package found with " +
            (params.containsKey("propertyId")
                ? "property id: " + params.get("propertyId")
                : "package name: " + params.get("packageName"));

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




















