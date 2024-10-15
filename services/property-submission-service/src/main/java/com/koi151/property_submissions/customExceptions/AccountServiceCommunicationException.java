package com.koi151.property_submissions.customExceptions;

public class AccountServiceCommunicationException extends RuntimeException {
    public AccountServiceCommunicationException(String message) {
        super(message);
    }
}
