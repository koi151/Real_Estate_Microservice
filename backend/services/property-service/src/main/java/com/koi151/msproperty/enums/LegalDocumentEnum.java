package com.koi151.msproperty.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.koi151.msproperty.customExceptions.InvalidEnumValueException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum LegalDocumentEnum {

    LAND_USE_RIGHT ("Land use right certificate"),
    SALE_CONTRACT ("Sale contract"),
    WAITING_CERTIFICATE("Waiting for certificate");

    private final String legalDocumentName;
    private static final LegalDocumentEnum[] VALUES = values(); // Cache enum values for performance

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static LegalDocumentEnum fromString(String s) {
        return s == null ? null
            : Arrays.stream(VALUES)
            .filter(value -> value.name().equals(s.toUpperCase()))
            .findFirst()
            .orElseThrow(() -> new InvalidEnumValueException("Invalid LegalDocumentEnum value: " + s));
    }
}
