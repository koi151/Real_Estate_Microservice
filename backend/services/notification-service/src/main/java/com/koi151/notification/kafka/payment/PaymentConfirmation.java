package com.koi151.notification.kafka.payment;

import com.koi151.notification.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PaymentConfirmation(
    String referenceCode,
    BigDecimal totalFee,
    PaymentMethod paymentMethod,
    String customerFirstName,
    String customerLastName,
    String customerEmail
) { }
