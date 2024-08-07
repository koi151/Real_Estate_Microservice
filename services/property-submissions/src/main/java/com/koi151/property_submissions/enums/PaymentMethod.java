package com.koi151.property_submissions.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.koi151.property_submissions.customExceptions.InvalidEnumValueException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum PaymentMethod {
    BANK_TRANSFER("Bank transfer"),
    DIGITAL_WALLET("Digital wallet"),
    CREDIT_CARD("Credit card"),
    VISA("Visa"),
    MASTER_CARD("Master Card");

    private final String typeName;

    private static final PaymentMethod[] VALUES = values(); // Cache enum values for performance

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static PaymentMethod fromString(String s) {
        return s == null ? null
                : Arrays.stream(VALUES)
                .filter(value -> value.name().equals(s.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new InvalidEnumValueException("Invalid PaymentMethod enum value: " + s));
    }
}
