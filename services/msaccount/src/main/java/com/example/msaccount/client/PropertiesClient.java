package com.example.msaccount.client;

import com.example.msaccount.model.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "property-service", url = "${application.config.properties-url}")
public interface PropertiesClient {

    @GetMapping("/account/{account-id}")
    ResponseEntity<ResponseData> findAllPropertiesByAccount(@PathVariable("account-id") Long accountId);

}
