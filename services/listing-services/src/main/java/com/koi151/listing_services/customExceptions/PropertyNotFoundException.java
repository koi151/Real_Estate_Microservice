package com.koi151.listing_services.customExceptions;

import java.io.Serial;

public class PropertyNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1917348136347635349L;

    public PropertyNotFoundException(String message) {
        super(message);
    }
}
