package com.koi151.msproperties.model.request.propertyForSale;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PropertyForSaleCreateRequest (

    @NotNull(message = "Property sale price cannot be null")
    @PositiveOrZero(message = "Property sale price must be positive or zero")
    @DecimalMax(value = "99999999999", message = "Sale price cannot exceed 99,999,999,999")
    BigDecimal salePrice,

    @Size(max = 5000, message = "Sale term cannot exceed {max} characters long")
    String saleTerm
) {}
