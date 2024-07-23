package com.koi151.property_submissions.customExceptions;

import java.io.Serial;

public class ProductServiceCommunicationException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = -1745635044615413382L;

    public ProductServiceCommunicationException(String message) {
        super(message);
    }
}
