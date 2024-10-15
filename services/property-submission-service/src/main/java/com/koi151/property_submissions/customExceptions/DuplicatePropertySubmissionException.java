package com.koi151.property_submissions.customExceptions;

public class DuplicatePropertySubmissionException extends RuntimeException{
    public DuplicatePropertySubmissionException(String message) {
        super(message);
    }
}
