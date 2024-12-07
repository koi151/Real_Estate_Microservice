package com.koi151.msproperty.customExceptions;

import java.io.Serial;

public class InvalidRequestException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 9079235060672233857L;

    public InvalidRequestException(String message) {
        super(message);
    }
}
