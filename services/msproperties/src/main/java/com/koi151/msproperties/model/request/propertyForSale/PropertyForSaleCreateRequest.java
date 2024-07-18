package com.koi151.msproperties.model.request.propertyForSale;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class PropertyForSaleCreateRequest {

    @NotNull(message = "Property sale price cannot be null")
    @PositiveOrZero(message = "Property sale price must be positive or zero")
    private double salePrice;

    private String saleTerm;
}
