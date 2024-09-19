package com.example.msaccount.customExceptions;

import java.io.Serial;

public class EntityNotFoundException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 4193087361754974990L;

    public EntityNotFoundException(String message) {
        super(message);
    }
}
