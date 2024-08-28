package com.koi151.payment.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.koi151.payment.customExceptions.InvalidEnumValueException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum PaymentMethod {
    CREDIT_CARD ("Credit card"),
    E_WALLET ("E-wallet"),
    ONLINE_BANKING ("Online banking"),
    VISA ("Visa"),
    MASTER_CARD ("Master Card"),
    PAYPAL ("Paypal");

    private final String methodName;

    private static final PaymentMethod[] VALUES = values();

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static PaymentMethod fromString(String s) {
        return s == null ? null
            : Arrays.stream(VALUES)
            .filter(value -> value.name().equals(s.toUpperCase()))
            .findFirst()
            .orElseThrow(() -> new InvalidEnumValueException("Invalid payment method enum value: " + s));
    }
}
