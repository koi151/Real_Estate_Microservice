package com.example.msaccount.customExceptions;

import java.io.Serial;

public class KeycloakAccountCreationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 134552894613796793L;

    public KeycloakAccountCreationException(String message) {
        super(message);
    }
}
