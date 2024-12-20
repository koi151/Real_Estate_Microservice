package com.koi151.msproperty.customExceptions;

import java.io.Serial;

public class EntityNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 6900878496102986593L;

    public EntityNotFoundException(String message) {
        super(message);
    }
}
