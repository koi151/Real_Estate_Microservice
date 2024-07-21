package com.koi151.ms_post_approval.customExceptions;

public class PropertyServiceResponseException extends RuntimeException {
    public PropertyServiceResponseException(String message) {
        super(message);
    }
}
