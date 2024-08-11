package com.koi151.listing_services.customExceptions;

import java.io.Serial;

public class PostServiceNotExistedException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -2943562432657566618L;

    public PostServiceNotExistedException(String message) {
        super(message);
    }
}
