package com.koi151.property_submissions.customExceptions;

import java.io.Serial;

public class AccountServiceResponseException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = -1095204546431462274L;

    public AccountServiceResponseException(String message) {
        super(message);
    }
}
