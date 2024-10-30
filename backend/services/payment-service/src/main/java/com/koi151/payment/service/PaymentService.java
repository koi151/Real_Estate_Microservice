package com.koi151.payment.service;

import com.koi151.payment.model.dto.PaymentCreateDTO;
import com.koi151.payment.model.request.PaymentCreateRequest;

public interface PaymentService {
    PaymentCreateDTO createPaymentFromSubmission(PaymentCreateRequest request);
}
