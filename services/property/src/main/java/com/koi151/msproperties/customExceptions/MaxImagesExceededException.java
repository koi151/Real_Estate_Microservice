package com.koi151.msproperties.customExceptions;

public class MaxImagesExceededException extends RuntimeException {
    public MaxImagesExceededException(String message) {
        super(message);
    }
}
