package com.koi151.property_submissions.model.request;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record PaymentCreateRequest(
    Long propertyId,
    BigDecimal totalFee,
    String orderInfo,
    String bankCode,
    String transactionNo,
    String paymentMethod,
    LocalDateTime payDate
) {}
