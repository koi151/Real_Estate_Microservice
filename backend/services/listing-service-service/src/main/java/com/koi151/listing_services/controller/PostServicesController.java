package com.koi151.listing_services.controller;

import com.koi151.listing_services.enums.PackageType;
import com.koi151.listing_services.model.request.PostServiceCreateRequest;
import com.koi151.listing_services.model.request.PostServicePricingCreateRequest;
import com.koi151.listing_services.model.response.ResponseData;
import com.koi151.listing_services.service.PostServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/listing-services/services")
public class PostServicesController {

    private final PostServiceService postServiceService;

    @GetMapping("/")
//    @PreAuthorize("hasAuthority('SCOPE_listing-service_view')")
    public ResponseEntity<ResponseData> findListingServices() {
        var services = postServiceService.findActiveServices();
        return ResponseEntity.ok(ResponseData.builder()
            .data(services)
            .desc("Get current active listing services successful")
            .build());
    }

    @PostMapping("/")
    public ResponseEntity<ResponseData> createPostService(
        @RequestBody @Valid PostServiceCreateRequest request
    ) {
        var postService = postServiceService.createPostService(request);
        String description = "Created post service successfully";

        var pricingsWithNullStartDate = request.postServicePricings().stream()
            .filter(pricing -> pricing.startDate() == null)
            .map(PostServicePricingCreateRequest::packageType) // Get the PackageType enum
            .map(PackageType::getPackageName)
            .toList();

        if (!pricingsWithNullStartDate.isEmpty()) {
            String pricingsString = pricingsWithNullStartDate.toString();
            description += ". The following post service pricings have their start date set to the current time by default: " + pricingsString;
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseData.builder()
            .desc(description)
            .data(postService)
            .build());
    }
}













