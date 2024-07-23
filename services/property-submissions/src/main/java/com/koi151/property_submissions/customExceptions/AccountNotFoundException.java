package com.koi151.property_submissions.customExceptions;

import java.io.Serial;

public class AccountNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -8253479052125615862L;

    public AccountNotFoundException(String message) {
        super(message);
    }
}
