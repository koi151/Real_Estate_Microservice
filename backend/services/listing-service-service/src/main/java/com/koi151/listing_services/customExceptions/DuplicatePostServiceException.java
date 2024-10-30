package com.koi151.listing_services.customExceptions;

import java.io.Serial;

public class DuplicatePostServiceException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1473199551310481149L;

    public DuplicatePostServiceException(String message) {
        super(message);
    }
}
