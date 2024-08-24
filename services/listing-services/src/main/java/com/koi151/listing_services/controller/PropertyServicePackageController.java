package com.koi151.listing_services.controller;

import com.koi151.listing_services.model.request.PropertyServicePackageCreateRequest;
import com.koi151.listing_services.model.response.ResponseData;
import com.koi151.listing_services.service.PropertyServicePackageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/listing-services")
public class PropertyServicePackageController {

    private final PropertyServicePackageService propertyServicePackageService;

    @GetMapping("/{id}/property-service-package")
    public ResponseEntity<ResponseData> findPropertyServicePackageById(@PathVariable(name = "id") Long id) {
        var result = propertyServicePackageService.findPropertyServicePackageWithsPostServices(id);
        return ResponseEntity.ok(ResponseData.builder()
            .data(result)
            .desc(result != null
                ? "Successfully get property service package with id: " + id
                : "No property service package found with id: " + id)
            .build());
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




















