package com.koi151.ms_post_approval.controllerAdvice;

import com.koi151.ms_post_approval.customExceptions.*;
import com.koi151.ms_post_approval.model.response.ErrorResponse;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.UnexpectedRollbackException;
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
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        ErrorResponse errorResponseDTO = new ErrorResponse();
        errorResponseDTO.setError("Validation failed");
        errorResponseDTO.setDetails(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }

    @ExceptionHandler(InvalidEnumValueException.class)
    public ResponseEntity<ErrorResponse> handleInvalidEnumValueException(InvalidEnumValueException ex) {

        List<String> details = new ArrayList<>();
        details.add("Recheck status value");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(ex.getMessage());
        errorResponse.setDetails(details);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DuplicatePropertySubmissionException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatePropertySubmissionException(DuplicatePropertySubmissionException ex) {

        List<String> details = new ArrayList<>();
        details.add("Property id existed, submission already send");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(ex.getMessage());
        errorResponse.setDetails(details);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotFoundException(AccountNotFoundException ex) {

        List<String> details = new ArrayList<>();
        details.add("Account do not have any property submission, recheck again");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(ex.getMessage());
        errorResponse.setDetails(details);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(DuplicateReferenceCodeException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateReferenceCodeException(DuplicateReferenceCodeException ex) {

        List<String> details = new ArrayList<>();
        details.add("Reference code of property submission existed, please create a new one");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(ex.getMessage());
        errorResponse.setDetails(details);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    // FEIGN CLIENT SERVICE //
    @ExceptionHandler(AccountServiceResponseException.class)
    public ResponseEntity<ErrorResponse> handleAccountServiceResponseException(AccountServiceResponseException ex) {

        List<String> details = new ArrayList<>();
        details.add("Failed to get properly response from Account service or invalid data response, recheck Account service again");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(ex.getMessage());
        errorResponse.setDetails(details);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(PropertyServiceResponseException.class)
    public ResponseEntity<ErrorResponse> handlePropertyServiceResponseException(PropertyServiceResponseException ex) {

        List<String> details = new ArrayList<>();
        details.add("Failed to get properly response from Property service or invalid data response, recheck Property service again");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(ex.getMessage());
        errorResponse.setDetails(details);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
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

                errorResponse.setError("Duplicate entry: ");
                errorResponse.setDetails(details);
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    // PROPERTY EXCEPTIONS
    @ExceptionHandler(PropertyNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePropertyNotFoundException(PropertyNotFoundException ex) {

        List<String> details = new ArrayList<>();
        details.add("Property not existed, recheck its information again");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(ex.getMessage());
        errorResponse.setDetails(details);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
