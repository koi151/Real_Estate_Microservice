package com.koi151.property_submissions.customExceptions;

import java.io.Serial;

public class ResourceNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 3174946846790540915L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
