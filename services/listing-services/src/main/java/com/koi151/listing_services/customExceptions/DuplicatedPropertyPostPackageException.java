package com.koi151.listing_services.customExceptions;

import java.io.Serial;

public class DuplicatedPropertyPostPackageException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 7187196504047430549L;

    public DuplicatedPropertyPostPackageException(String message) {
        super(message);
    }
}
