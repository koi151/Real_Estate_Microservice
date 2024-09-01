package com.koi151.notification.customExceptions;

import java.io.Serial;

public class InvalidEnumValueException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -4702412683132512264L;

    public InvalidEnumValueException(String message) {
        super(message);
    }
}
