package com.koi151.msproperty.model.request.propertyForRent;

import com.koi151.msproperty.annotation.PriceRangeConstraint;
import com.koi151.msproperty.enums.PaymentScheduleEnum;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@PriceRangeConstraint
public record PropertyForRentSearchRequest(
    @PositiveOrZero(message = "Property rental max price searching must be non-negative value")
    @DecimalMax(value = "9000000000", message = "Property rental max price cannot exceed 90,000,000,000")
    BigDecimal priceFrom,

    @PositiveOrZero(message = "Property rental max price searching must be non-negative value")
    @DecimalMax(value = "99999999999", message = "Property rental max price cannot exceed 99,999,999,999")
    BigDecimal priceTo,

    PaymentScheduleEnum paymentSchedule,

    @Size(max = 1000, message = "Rental terms searching cannot exceed {max} characters")
    String rentalTerm
) {}
