package com.example.msaccount.customExceptions;

import java.io.Serial;

public class KeycloakLoginFailedException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -2732705711231659409L;

    public KeycloakLoginFailedException(String message) {
        super(message);
    }
}
