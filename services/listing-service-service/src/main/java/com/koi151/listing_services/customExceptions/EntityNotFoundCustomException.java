package com.koi151.listing_services.customExceptions;

import java.io.Serial;

public class EntityNotFoundCustomException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 3162458355174474328L;

    public EntityNotFoundCustomException(String message) {
        super(message);
    }
}
