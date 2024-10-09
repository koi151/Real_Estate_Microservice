package com.example.msaccount.customExceptions;

import java.io.Serial;

public class RemoteServiceException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 4224400523307348885L;

    public RemoteServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
