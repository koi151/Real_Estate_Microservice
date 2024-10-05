package com.example.msaccount.customExceptions;

import java.io.Serial;

public class KeycloakRoleRetrievalException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 6314156473471885911L;

    public KeycloakRoleRetrievalException(String message) {
        super(message);
    }
}
