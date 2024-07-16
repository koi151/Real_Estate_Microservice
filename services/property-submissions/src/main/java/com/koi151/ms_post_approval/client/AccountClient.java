package com.koi151.ms_post_approval.client;

import com.koi151.ms_post_approval.model.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "account-service", url = "${application.config.admin-account-url}")
public interface AccountClient {
    @GetMapping("/{account-id}/name-and-role")
    ResponseEntity<ResponseData> getAccountNameAndRole(
            @PathVariable("account-id") Long accountId
    );
}