package com.koi151.listing_services.client;

import com.koi151.listing_services.model.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "property-service", url = "${application.config.properties-url}")
public interface PropertyClient {
    @GetMapping("/{id}/exists")
    ResponseEntity<ResponseData> propertyExistsCheck(@PathVariable(name = "id") Long id);
}
