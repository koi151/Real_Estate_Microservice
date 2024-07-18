package com.koi151.msproperties.model.request.propertyForRent;

import com.koi151.msproperties.enums.PaymentScheduleEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PropertyForRentCreateRequest {

    @NotNull(message = "Property rental price cannot be null")
    @PositiveOrZero(message = "Property rental price must be positive or zero")
    private double rentalPrice;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Payment schedule cannot be null")
    private PaymentScheduleEnum paymentSchedule;

    private String rentalTerm;
}
