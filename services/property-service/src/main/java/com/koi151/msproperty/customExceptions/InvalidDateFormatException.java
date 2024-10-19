package com.koi151.msproperty.customExceptions;

public class InvalidDateFormatException extends RuntimeException { // You can extend another exception if you prefer.

    public InvalidDateFormatException(String message) {
        super(message);
    }
}