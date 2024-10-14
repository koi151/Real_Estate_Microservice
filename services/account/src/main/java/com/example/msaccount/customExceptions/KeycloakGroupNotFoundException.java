package com.example.msaccount.customExceptions;

import java.io.Serial;

public class KeycloakGroupNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 4963662980385428517L;

    public KeycloakGroupNotFoundException(String message) {
        super(message);
    }
}
