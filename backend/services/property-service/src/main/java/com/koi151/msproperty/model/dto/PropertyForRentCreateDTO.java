package com.koi151.msproperty.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyForRentCreateDTO {

    private Long propertyId;
    private double rentalPrice;
    private String paymentSchedule;
    private String rentalTerms;
}
