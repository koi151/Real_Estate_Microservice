package com.example.msaccount.customExceptions;

import java.io.Serial;

public class InvalidTokenException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 9079235060672233857L;

    public InvalidTokenException(String message) {
        super(message);
    }
}
