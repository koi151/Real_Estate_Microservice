package com.koi151.msproperty.model.request.propertyForRent;

import com.koi151.msproperty.enums.PaymentScheduleEnum;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record PropertyForRentUpdateRequest (

    @PositiveOrZero(message = "Property rental price must be non-negative value")
    @DecimalMax(value = "99999999999", message = "Rental price cannot exceed 99,999,999,999")
    BigDecimal rentalPrice,
    PaymentScheduleEnum paymentSchedule,
    @Size(max = 5000, message = "Rental terms cannot exceed {max} characters")
    String rentalTerms
) {}
