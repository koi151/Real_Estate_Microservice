package com.koi151.property_submissions.kafka;

import com.koi151.property_submissions.enums.PaymentMethod;
import com.koi151.property_submissions.model.response.CustomerResponse;
import com.koi151.property_submissions.model.response.PropertyServicePackageResponse;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record SubmissionConfirmation(
    String referenceCode,
    PaymentMethod paymentMethod,
    CustomerResponse customer,
    PropertyServicePackageResponse propertyServicePackage
) {}
