package com.koi151.ms_post_approval.customExceptions;

public class DuplicatePropertySubmissionException extends RuntimeException{
    public DuplicatePropertySubmissionException(String message) {
        super(message);
    }
}
