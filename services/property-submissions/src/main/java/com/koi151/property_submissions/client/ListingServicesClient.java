package com.koi151.property_submissions.client;

import com.koi151.property_submissions.model.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "listing-services", url = "${application.config.listing-services-url}")
public interface ListingServicesClient {

    @GetMapping("/property-service-package/")
    ResponseEntity<ResponseData> findPropertyServicePackageByCriteria(
        @RequestParam Map<String, String> params
    );
}
