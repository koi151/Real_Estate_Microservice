package com.example.msaccount.customExceptions;

import java.io.Serial;

public class KeycloakResourceNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -3720556479497975002L;

    public KeycloakResourceNotFoundException(String message) {
        super(message);
    }
}
