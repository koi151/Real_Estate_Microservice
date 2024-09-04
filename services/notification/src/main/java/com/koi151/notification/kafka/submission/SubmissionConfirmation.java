package com.koi151.notification.kafka.submission;

import com.koi151.notification.enums.PaymentMethod;

import java.math.BigDecimal;

public record SubmissionConfirmation (
    String referenceCode,
    BigDecimal totalAmount,
    PaymentMethod paymentMethod,
    Customer customer,
    PropertyServicePackage propertyServicePackage
) {}
