package com.koi151.listing_services.model.request;

import com.koi151.listing_services.enums.Status;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

public record PostServiceCreateRequest (
    @NotNull(message = "Post service category id is mandatory")
    Long postServiceCategoryId,

    @NotNull(message = "Post service name is mandatory")
    @Size(min = 5, max = 100, message = "Post service name must between {min} and {max} characters")
    String name,

    @NotNull(message = "Post service available unit quantity is mandatory")
    @Positive(message = "Post service available unit quantity must greater than zero")
    Integer availableUnits,

    Status status,
    @Size(max = 2000, message = "Post service description cannot exceed {max} characters")
    String description,
    List<PostServicePricingCreateRequest> postServicePricings,
    List<PromotionCreateRequest> promotions,
    @Future(message = "Posting date must be in the future")
    LocalDateTime postingDate
) {}
