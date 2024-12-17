package com.koi151.notification.kafka.submission;

import com.koi151.notification.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record SubmissionConfirmation (
    String referenceCode,
    BigDecimal totalAmount,
    PaymentMethod paymentMethod,
    Customer customer,
    PropertyServicePackage propertyServicePackage
) {}
