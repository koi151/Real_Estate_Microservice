package com.koi151.property_submissions.kafka;

import com.koi151.property_submissions.enums.PaymentMethod;
import com.koi151.property_submissions.model.response.CustomerResponse;
import com.koi151.property_submissions.model.response.PurchaseResponse;

import java.math.BigDecimal;

public record SubmissionConfirmation(
    String referenceCode,
    BigDecimal totalAmount,
    PaymentMethod paymentMethod,
    CustomerResponse customerResponse,
    PurchaseResponse purchaseResponse
) {}
