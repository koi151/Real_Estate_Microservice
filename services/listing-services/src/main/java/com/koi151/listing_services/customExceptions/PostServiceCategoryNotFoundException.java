package com.koi151.listing_services.customExceptions;

import java.io.Serial;

public class PostServiceCategoryNotFoundException extends  RuntimeException {
    @Serial
    private static final long serialVersionUID = 6916008807224947037L;

    public PostServiceCategoryNotFoundException(String message) {
        super(message);
    }
}
