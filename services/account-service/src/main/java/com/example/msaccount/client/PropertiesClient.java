package com.example.msaccount.client;

import com.example.msaccount.model.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "property-service", url = "${application.config.properties-url}")
public interface PropertiesClient {

    @GetMapping("/account/{account-id}")
    ResponseEntity<ResponseData> findAllPropertiesByAccount(
            @PathVariable("account-id") String accountId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int limit
    );

}
