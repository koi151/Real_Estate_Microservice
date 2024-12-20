package com.koi151.msproperty.model.request.propertyForSale;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;


import java.math.BigDecimal;

public record PropertyForSaleUpdateRequest (
    @PositiveOrZero(message = "Property sale price must be positive or zero")
    @DecimalMax(value = "99999999999", message = "Rental price cannot exceed 99,999,999,999")
    BigDecimal salePrice,

    @Size(max = 5000, message = "Sale term cannot exceed {max} characters long")
    String saleTerms
) {}
