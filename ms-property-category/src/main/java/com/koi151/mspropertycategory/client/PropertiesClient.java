package com.koi151.mspropertycategory.client;

import com.koi151.mspropertycategory.entity.Properties;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "property-service", url = "${application.config.properties-url}")
public interface PropertiesClient {

    @GetMapping("/category/{category-id}")
    List<Properties> findAllPropertiesByCategory(@PathVariable("category-id") Integer categoryId);
}
