package com.koi151.ms_post_approval.model.request;

import com.koi151.ms_post_approval.enums.PaymentMethod;
import com.koi151.ms_post_approval.enums.PostStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

public record PropertySubmissionCreate (
    @NotNull(message = "Property id cannot be null")
    Long propertyId,
    @NotNull(message = "Account id cannot be null")
    Long accountId,
    Long reviewerId,
    @NotBlank(message = "Reference code cannot be blank")
    @Size(max = 100, message = "Reference code cannot longer than {max} characters")
    String referenceCode,
    PostStatus status,
    PaymentMethod paymentMethod,
    @Size(max = 3000, message = "Review message cannot exceed {max} characters")
    String reviewMessage
) {
}
