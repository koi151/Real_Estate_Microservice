package com.koi151.property_submissions.customExceptions;

import java.io.Serial;

public class ServiceUnavailableException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 3110147929927729854L;

    public ServiceUnavailableException(String message) {
        super(message);
    }
}
