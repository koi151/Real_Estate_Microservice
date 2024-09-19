package com.example.msaccount.customExceptions;

import java.io.Serial;

public class EntityAlreadyExistsException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 5875429336163883270L;

    public EntityAlreadyExistsException(String message) {
        super(message);
    }
}
