package com.koi151.property_submissions.model.request;

import com.koi151.property_submissions.model.response.CustomerResponse;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record PaymentCreateRequest(
    Long propertyId,
    String referenceCode,
    BigDecimal totalFee,
    String orderInfo,
    String bankCode,
    // Temp fields for testing ======================
    String transactionNo,
    LocalDateTime payDate,
    //===========================
    String paymentMethod,
    CustomerResponse customer
) {}
