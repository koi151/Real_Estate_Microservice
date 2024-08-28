package com.koi151.payment.controller;

import com.koi151.payment.model.request.PaymentCreateRequest;
import com.koi151.payment.model.response.ResponseData;
import com.koi151.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/")
    public ResponseEntity<ResponseData> createPaymentFromSubmission(@RequestBody @Valid PaymentCreateRequest request) {
        var paymentResult = paymentService.createPaymentFromSubmission(request);
        return new ResponseEntity<>(
            ResponseData.builder()
                .data(paymentResult)
                .description("Succeed")
                .build(),
            HttpStatus.CREATED
        );
    }
}
