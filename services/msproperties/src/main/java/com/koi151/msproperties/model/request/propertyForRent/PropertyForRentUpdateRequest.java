package com.koi151.msproperties.model.request.propertyForRent;

import com.koi151.msproperties.enums.PaymentScheduleEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyForRentUpdateRequest {

    @PositiveOrZero(message = "Property rental price must be positive or zero")
    private double rentalPrice;

    @Enumerated(EnumType.STRING)
    private PaymentScheduleEnum paymentSchedule;

    private String rentalTerms;
}
