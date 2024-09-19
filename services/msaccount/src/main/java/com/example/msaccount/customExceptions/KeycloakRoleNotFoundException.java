package com.example.msaccount.customExceptions;

import java.io.Serial;

public class KeycloakRoleNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1188116763089123909L;

    public KeycloakRoleNotFoundException(String message) {
        super(message);
    }
}
