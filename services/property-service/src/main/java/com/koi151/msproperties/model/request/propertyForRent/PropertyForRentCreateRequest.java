package com.koi151.msproperties.model.request.propertyForRent;

import com.koi151.msproperties.enums.PaymentScheduleEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
public record PropertyForRentCreateRequest (

    @NotNull(message = "Property rental price is mandatory")
    @PositiveOrZero(message = "Property rental price must be non-negative value")
    @DecimalMax(value = "99999999999", message = "Rental price cannot exceed 99,999,999,999")
    BigDecimal rentalPrice,

    @NotNull(message = "Payment schedule is mandatory")
    PaymentScheduleEnum paymentSchedule,

    @Size(max = 5000, message = "Rental terms cannot exceed {max} characters")
    String rentalTerm
) {}
