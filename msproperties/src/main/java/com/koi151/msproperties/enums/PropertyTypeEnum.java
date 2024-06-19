package com.koi151.msproperties.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PropertyTypeEnum {
    RENT("For rent"),
    SALE("For sale");

    private final String propertyType;

    public boolean isPropertyForRent() {
        return this == RENT;
    }

    public boolean isPropertyForSale() {
        return this == SALE;
    }
}
