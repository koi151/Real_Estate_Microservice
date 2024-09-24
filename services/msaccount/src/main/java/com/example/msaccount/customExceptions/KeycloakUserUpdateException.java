package com.example.msaccount.customExceptions;

import java.io.Serial;

public class KeycloakUserUpdateException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -2990407686850232081L;

    public KeycloakUserUpdateException(String message) {
        super(message);
    }
}
