package com.koi151.payment.customExceptions;

public class InvalidEnumValueException extends RuntimeException{
    public InvalidEnumValueException(String message) {
        super(message);
    }
}
