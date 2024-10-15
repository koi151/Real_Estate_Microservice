package com.koi151.property_submissions.model.request;

import com.koi151.property_submissions.enums.PaymentMethod;
import com.koi151.property_submissions.enums.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PropertySubmissionCreate (
    @NotNull(message = "Property id cannot be null")
    Long propertyId,
    @NotNull(message = "Account id cannot be null")
    Long accountId,
    Long reviewerId,
    @NotBlank(message = "Reference code cannot be blank")
    @Size(max = 50, message = "Reference code cannot exceed {max} characters")
    String referenceCode,
    PostStatus status,
    @NotNull(message = "Payment method is mandatory")
    PaymentMethod paymentMethod,
    @Size(max = 3000, message = "Review message cannot exceed {max} characters")
    String reviewMessage
) {
}
