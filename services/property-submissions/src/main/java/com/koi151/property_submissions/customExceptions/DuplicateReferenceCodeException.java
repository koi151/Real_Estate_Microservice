package com.koi151.property_submissions.customExceptions;

import java.io.Serial;

public class DuplicateReferenceCodeException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 8394870969003187611L;

    public DuplicateReferenceCodeException(String message) {
        super(message);
    }
}
