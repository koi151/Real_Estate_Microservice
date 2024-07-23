package com.koi151.property_submissions.model.response;

import com.koi151.property_submissions.enums.PaymentMethod;
import com.koi151.property_submissions.enums.PostStatus;

public record PurchaseResponse (
    Long propertyId,
    Long accountId,
    Long reviewerId,
    String referenceCode,
    PostStatus status,
    PaymentMethod paymentMethod,
    String reviewMessage
){}
