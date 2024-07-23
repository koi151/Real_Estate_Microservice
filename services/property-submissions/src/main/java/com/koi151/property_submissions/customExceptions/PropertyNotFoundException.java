package com.koi151.property_submissions.customExceptions;

import java.io.Serial;

public class PropertyNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6646597083860115719L;

    public PropertyNotFoundException(String message) {
        super(message);
    }
}
