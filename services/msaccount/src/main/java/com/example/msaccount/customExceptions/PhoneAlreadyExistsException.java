package com.example.msaccount.customExceptions;

public class PhoneAlreadyExistsException extends RuntimeException {
    public PhoneAlreadyExistsException(String message) {
        super(message);
    }
}
