package com.koi151.msproperties.model.request.propertyForSale;

import com.koi151.msproperties.annotation.PriceRangeConstraint;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@PriceRangeConstraint
public record PropertyForSaleSearchRequest(
    @PositiveOrZero(message = "Property sale max price searching must be non-negative value")
    @DecimalMax(value = "9000000000", message = "Property rental max price cannot exceed 90,000,000,000")
    BigDecimal priceFrom,

    @PositiveOrZero(message = "Property sale max price searching must be non-negative value")
    @DecimalMax(value = "99999999999", message = "Property rental max price cannot exceed 99,999,999,999")
    BigDecimal priceTo,

    @Size(max = 1000, message = "Sale term searching cannot exceed {max} characters long")
    String saleTerms
) {}
