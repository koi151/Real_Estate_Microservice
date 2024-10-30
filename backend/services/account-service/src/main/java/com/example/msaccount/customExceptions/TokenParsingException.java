package com.example.msaccount.customExceptions;

import java.io.Serial;

public class TokenParsingException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = -5463836049505199773L;

    public TokenParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
