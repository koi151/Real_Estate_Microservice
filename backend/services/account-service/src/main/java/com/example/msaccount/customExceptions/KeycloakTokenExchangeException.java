package com.example.msaccount.customExceptions;

public class KeycloakTokenExchangeException extends RuntimeException{
    public KeycloakTokenExchangeException(String message) {
        super(message);
    }
}
