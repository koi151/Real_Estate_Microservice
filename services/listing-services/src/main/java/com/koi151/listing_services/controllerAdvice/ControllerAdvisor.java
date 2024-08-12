package com.koi151.listing_services.controllerAdvice;

import com.koi151.listing_services.customExceptions.*;
import com.koi151.listing_services.model.response.ErrorResponse;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(InvalidEnumValueException.class)
    public ResponseEntity<ErrorResponse> handleInvalidEnumValueException(InvalidEnumValueException ex) {

        List<String> details = new ArrayList<>();
        details.add("Recheck status value");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(ex.getMessage());
        errorResponse.setDetails(details);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult().getFieldErrors()
            .stream() // create new stream
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.toList()); // convert to list

        ErrorResponse errorResponseDTO = new ErrorResponse();
        errorResponseDTO.setError("Validation failed");
        errorResponseDTO.setDetails(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }

    // CONSTRAIN VIOLATION //
    @ExceptionHandler(ConstraintViolationException.class) // using for common case
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        Throwable cause = ex.getCause();
        ErrorResponse errorResponse = new ErrorResponse();

        if (cause instanceof SQLIntegrityConstraintViolationException sqlEx) {
            String message = cause.getMessage();

            List<String> details = new ArrayList<>();
            details.add(message);

            errorResponse.setError("Data validate failed: ");
            errorResponse.setDetails(details);
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(PostServiceCategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePostServiceCategoryNotFoundException(PostServiceCategoryNotFoundException ex) {
        List<String> details = new ArrayList<>();
        details.add("Post service category not exists, recheck again");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setDetails(details);
        errorResponse.setError(ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    // duplicate ex
    @ExceptionHandler(DuplicatePostServiceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatePostServiceException(DuplicatePostServiceException ex) {
        List<String> details = new ArrayList<>();
        details.add("Post service validate failed, recheck again");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setDetails(details);
        errorResponse.setError(ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(DuplicatePostServiceCategoryException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatePostServiceCategoryException(DuplicatePostServiceCategoryException ex) {
        List<String> details = new ArrayList<>();
        details.add("Post service category validate failed, recheck again");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setDetails(details);
        errorResponse.setError(ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    // NOT EXISTS EX ==================================
    @ExceptionHandler(PostServiceNotExistedException.class)
    public ResponseEntity<ErrorResponse> handlePostServiceNotExistedException(PostServiceNotExistedException ex) {
        List<String> details = new ArrayList<>();
        details.add("Post service not existed, recheck again");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setDetails(details);
        errorResponse.setError(ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(PropertyNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePropertyNotFoundException(PropertyNotFoundException ex) {
        List<String> details = new ArrayList<>();
        details.add("Property not existed, recheck again");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setDetails(details);
        errorResponse.setError(ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}
