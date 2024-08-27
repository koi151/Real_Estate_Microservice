package com.koi151.property_submissions.model.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
    LocalDateTime payDate,
    String status
) {}
