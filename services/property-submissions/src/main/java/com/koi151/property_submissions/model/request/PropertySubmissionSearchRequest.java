package com.koi151.property_submissions.model.request;

import com.koi151.property_submissions.enums.PaymentMethod;
import com.koi151.property_submissions.enums.PostStatus;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record PropertySubmissionSearchRequest (
    Long propertySubmissionId,
    @Positive(message = "Property id must be positive value")
    Long propertyId,
    Long accountId,
    @Size(max = 100, message = "Reference code cannot exceed 100 characters")
    String referenceCode,
    @Size(max = 200, message = "Review message searching cannot exceed {max} characters")
    String reviewMessage,
    PaymentMethod paymentMethod,
    Long reviewerId,
    PostStatus status
) {}