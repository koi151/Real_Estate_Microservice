package com.koi151.listing_services.model.request;

import com.koi151.listing_services.enums.PackageType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PostServicePricingCreateRequest (
    @NotNull(message = "Post service pricing cannot be empty")
    @PositiveOrZero(message = "Post service pricing must be non-negative value")
    @DecimalMax(value = "9999999999.99", message = "Post service pricing cannot exceed 9,999,999,999.99")
    BigDecimal price,
    @NotNull(message = "Post service package is mandatory")
    PackageType packageType,
    @Future(message = "Post service pricing start date mst be in the future")
    @CreatedDate
    LocalDateTime startDate,
    LocalDateTime endDate
) {}

