package com.koi151.property_submissions.client;

import com.koi151.property_submissions.model.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "property-service", url = "${application.config.property-url}")
public interface PropertyClient {
    @GetMapping("/{id}/active")
    ResponseEntity<ResponseData> propertyActiveCheck (@PathVariable("id") Long accountId);
}
