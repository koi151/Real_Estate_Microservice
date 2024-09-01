package com.koi151.notification.kafka.submission;

import com.koi151.notification.enums.PaymentMethod;

public record SubmissionConfirmation (
    String referenceCode,
    PaymentMethod paymentMethod,
    Customer customerResponse,
    PropertyServicePackage propertyServicePackageResponse
) {}
