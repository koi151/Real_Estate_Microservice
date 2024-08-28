package com.koi151.property_submissions.client;

import com.koi151.property_submissions.model.request.PaymentCreateRequest;
import com.koi151.property_submissions.model.response.ResponseData;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service", url = "${application.config.payment-url}")
public interface PaymentClient {
    @PostMapping("/")
    ResponseEntity<ResponseData> createPaymentFromSubmission(@RequestBody @Valid PaymentCreateRequest request);
}
