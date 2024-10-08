package com.example.msaccount.customExceptions;

import java.io.Serial;

public class InvalidAuthorizationHeaderException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -4811005514527159742L;

    public InvalidAuthorizationHeaderException(String message) {
        super(message);
    }
}
