package com.koi151.payment.notification;

import com.koi151.payment.enums.PaymentMethod;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PaymentNotificationRequest (
    String referenceCode,
    BigDecimal totalFee,
    PaymentMethod paymentMethod,
    String customerFirstName,
    String customerLastName,
    String customerEmail
) {}
