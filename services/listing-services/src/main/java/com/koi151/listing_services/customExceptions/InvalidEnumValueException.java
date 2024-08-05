package com.koi151.listing_services.customExceptions;

import java.io.Serial;

public class InvalidEnumValueException extends RuntimeException {
    public InvalidEnumValueException(String message) {
        super(message);
    }

    @Serial
    private static final long serialVersionUID = -8172999312020966020L;
}
