package com.koi151.msproperty.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyForSaleCreateDTO {
    private Long propertyId;
    private double salePrice;
    private String saleTerms;
}
