package com.koi151.msproperties.model.request.propertyForRent;

import com.koi151.msproperties.enums.PaymentScheduleEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public record PropertyForRentUpdateRequest (

    @PositiveOrZero(message = "Property rental price must be non-negative value")
    @DecimalMax(value = "99_999_999_999", message = "Rental price cannot exceed 99,999,999,999")
    BigDecimal rentalPrice,
    PaymentScheduleEnum paymentSchedule,
    @Size(max = 5000, message = "Rental terms cannot exceed {max} characters")
    String rentalTerms
) {}
