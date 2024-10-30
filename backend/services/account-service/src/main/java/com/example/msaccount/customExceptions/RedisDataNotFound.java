package com.example.msaccount.customExceptions;

import java.io.Serial;

public class RedisDataNotFound extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -7680817327449611483L;

    public RedisDataNotFound(String message) {
        super(message);
    }
}
