package com.koi151.listing_services.customExceptions;

import java.io.Serial;

public class FailedToDeserializingData extends RuntimeException{
    @Serial
    private static final long serialVersionUID = -4466543117513065456L;

    public FailedToDeserializingData(String message) {
        super(message);
    }
}
