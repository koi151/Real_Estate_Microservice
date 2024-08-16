package com.koi151.listing_services.customExceptions;

import java.io.Serial;

public class ServiceUnavailableException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6151489432471369448L;

    public ServiceUnavailableException(String message) {
        super(message);
    }
}
