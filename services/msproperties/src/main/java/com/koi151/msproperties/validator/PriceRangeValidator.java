package com.koi151.msproperties.validator;


import com.koi151.msproperties.annotation.PriceRangeConstraint;
import com.koi151.msproperties.model.request.propertyForRent.PropertyForRentSearchRequest;
import com.koi151.msproperties.model.request.propertyForSale.PropertyForSaleSearchRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class PriceRangeValidator implements ConstraintValidator<PriceRangeConstraint, Object> {
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (obj instanceof PropertyForRentSearchRequest rentRequest) {
            return validatePriceRange(rentRequest.priceFrom(), rentRequest.priceTo());
        } else if (obj instanceof PropertyForSaleSearchRequest saleRequest) {
            return validatePriceRange(saleRequest.priceFrom(), saleRequest.priceTo());
        }
        // Handle other types if necessary
        return false;
    }

    private boolean validatePriceRange(BigDecimal priceFrom, BigDecimal priceTo) {
        return priceFrom == null || priceTo == null || priceFrom.compareTo(priceTo) <= 0;
    }
}
