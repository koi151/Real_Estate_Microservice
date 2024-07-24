package com.koi151.property_submissions.customExceptions;

public class ServiceCommunicationException extends RuntimeException{
    public ServiceCommunicationException(String message) {
        super(message);
    }
}
