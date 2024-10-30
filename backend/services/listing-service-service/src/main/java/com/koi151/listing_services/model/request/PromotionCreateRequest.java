package com.koi151.listing_services.model.request;

import com.koi151.listing_services.enums.PackageType;
import jakarta.persistence.EnumType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Max;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PromotionCreateRequest (
    @Max(value = 100, message = "Discount percentage cannot exceed {max}%")
    BigDecimal discountPercentage,
    PackageType packageType,
    @DecimalMax(value = "999999999.99", message = "Price discount cannot exceed {max}")
    BigDecimal priceDiscount
) {}
