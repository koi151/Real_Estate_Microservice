package com.koi151.listing_services.customExceptions;

import java.io.Serial;

public class DuplicatePostServiceCategoryException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 4224253436985250882L;

    public DuplicatePostServiceCategoryException(String message) {
        super(message);
    }
}
