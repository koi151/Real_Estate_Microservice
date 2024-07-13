package com.koi151.ms_post_approval.model.request;

import com.koi151.ms_post_approval.enums.PostStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PropertySubmissionCreate {

    @NotNull(message = "Property id cannot be null")
    @Positive(message = "Property id must be positive value")
    private Long propertyId;

    @NotNull(message = "Client id cannot be null")
    @Positive(message = "Client id must be positive value")
    private Long clientId;

    private Long reviewerId;

    @NotBlank(message = "Reference code cannot be blank")
    @Size(max = 100, message = "Reference code cannot longer than 100 characters")
    private String referenceCode;

    private PostStatus status;

    @Size(max = 3000, message = "Review message cannot exceed 3000 characters")
    private String reviewMessage;
}
