package com.example.msaccount.customExceptions;

public class CloudinaryUploadFailedException extends RuntimeException {
    public CloudinaryUploadFailedException(String message) {
        super(message);
    }
}
