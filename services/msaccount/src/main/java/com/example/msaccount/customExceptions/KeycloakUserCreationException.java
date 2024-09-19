package com.example.msaccount.customExceptions;

import java.io.Serial;

public class KeycloakUserCreationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 2145097782393670527L;

    public KeycloakUserCreationException(String message) {
        super(message);
    }
}
