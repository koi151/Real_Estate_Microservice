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

    @GetMapping("/{id}/post-services")
    public ResponseEntity<ResponseData> findPropertyPostServicesById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(ResponseData.builder()
            .data(listingServicesService.findPropertyServicePackageWithsPostServices(id))
            .desc("Get property post service with id: " + id + " succeed")
            .build());
    }


    @PostMapping("/")
    public ResponseEntity<ResponseData> createPropertyServicePackage(@RequestBody @Valid PropertyServicePackageCreateRequest request) {
        var propertyServicePackage = listingServicesService.createPostServicePackage(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseData.builder()
            .data(propertyServicePackage)
            .desc("Property service package created successfully")
            .build());
    }
}




















