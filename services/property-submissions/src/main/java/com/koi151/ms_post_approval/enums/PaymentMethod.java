package com.koi151.ms_post_approval.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentMethod {
    BANK_TRANSFER("Bank transfer"),
    DIGITAL_WALLET("Digital wallet"),
    CREDIT_CARD("Credit card"),
    VISA("Visa"),
    MASTER_CARD("Master Card");

    private final String type;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    private PaymentMethod fromString(String s) {
        return s == null ? null : PaymentMethod.valueOf(s.toUpperCase());
    }
}
