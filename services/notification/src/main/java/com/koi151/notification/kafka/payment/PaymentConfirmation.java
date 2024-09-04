package com.koi151.notification.kafka.payment;

import com.koi151.notification.enums.PaymentMethod;

import java.math.BigDecimal;

public record PaymentConfirmation(
    String referenceCode,
    BigDecimal totalFee,
    PaymentMethod paymentMethod,
    String customerFirstName,
    String customerLastName,
    String customerEmail
) { }
