package com.koi151.msproperty.client;

import com.koi151.msproperty.model.request.PropertyServicePackageCreateRequest;
import com.koi151.msproperty.model.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "listing-service-service", url = "localhost:8090/api/v1/listing-services")
public interface PropertyClient {
    @PostMapping("/property-service-package/")
    ResponseEntity<ResponseData> createPropertyServicePackage(@RequestBody PropertyServicePackageCreateRequest request);
}
