package com.koi151.property_submissions.client;

import com.koi151.property_submissions.model.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "listing-services", url = "${application.config.listing-services-url}")
public interface ListingServicesClient {

    @GetMapping("{id}/post-services")
    ResponseEntity<ResponseData> findPropertyPostServicesById(@PathVariable(name = "id") Long id);

}
