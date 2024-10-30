package com.koi151.property_submissions.customExceptions;

public class PropertyServiceResponseException extends RuntimeException {
    public PropertyServiceResponseException(String message) {
        super(message);
    }
}
