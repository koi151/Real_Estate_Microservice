package com.koi151.msproperties.model.dto;

import com.koi151.msproperties.enums.PaymentScheduleEnum;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyForRentCreateDTO {

    private Long propertyId;
    private double rentalPrice;
    private String paymentSchedule;
    private String rentTerm;
}
