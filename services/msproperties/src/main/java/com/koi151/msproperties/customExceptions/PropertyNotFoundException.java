package com.koi151.msproperties.customExceptions;

public class PropertyNotFoundException extends RuntimeException {
    public PropertyNotFoundException(String message) {
        super(message);
    }
}
