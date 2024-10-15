package com.koi151.listing_services.customExceptions;

import java.io.Serial;

public class ValidationFailedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -7907666311202255035L;

    public ValidationFailedException(String message) {
        super(message);
    }
}
