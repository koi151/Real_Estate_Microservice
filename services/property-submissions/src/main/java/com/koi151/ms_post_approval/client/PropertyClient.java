package com.koi151.ms_post_approval.client;

import com.koi151.ms_post_approval.model.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "property-service", url = "${application.config.property-url}")
public interface PropertyClient {
    @GetMapping("/{id}/exists")
    ResponseEntity<ResponseData> propertyExistsCheck (@PathVariable("id") Long accountId);
}
