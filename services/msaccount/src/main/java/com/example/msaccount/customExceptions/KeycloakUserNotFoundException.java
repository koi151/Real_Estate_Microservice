package com.example.msaccount.customExceptions;

import java.io.Serial;

public class KeycloakUserNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -3720556479497975002L;

    public KeycloakUserNotFoundException(String message) {
        super(message);
    }
}
