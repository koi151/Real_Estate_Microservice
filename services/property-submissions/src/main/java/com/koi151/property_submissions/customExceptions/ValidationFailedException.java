package com.koi151.property_submissions.customExceptions;

import java.io.Serial;

public class ValidationFailedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 3425870666331816388L;

    public ValidationFailedException(String message) {
        super(message);
    }
}
