package com.example.msaccount.customExceptions;

public class InvalidEnumValueException extends RuntimeException{
    public InvalidEnumValueException(String message) {
        super(message);
    }
}
