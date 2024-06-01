package com.koi151.mspropertycategory.client;

import com.koi151.mspropertycategory.entity.Properties;
import com.koi151.mspropertycategory.entity.payload.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "property-service", url = "${application.config.properties-url}")
public interface PropertiesClient {

    @GetMapping("/category/{category-id}")
    ResponseEntity<ResponseData> findAllPropertiesByCategory(@PathVariable("category-id") Integer categoryId);
}
