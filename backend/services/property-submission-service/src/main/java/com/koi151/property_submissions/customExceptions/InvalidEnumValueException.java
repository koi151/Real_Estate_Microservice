package com.koi151.property_submissions.customExceptions;

import java.io.Serial;

public class InvalidEnumValueException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -5091971512128928735L;

    public InvalidEnumValueException(String message) {
        super(message);
    }
}
