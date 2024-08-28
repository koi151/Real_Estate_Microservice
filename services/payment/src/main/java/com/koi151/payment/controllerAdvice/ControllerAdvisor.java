package com.koi151.payment.controllerAdvice;

import com.koi151.payment.customExceptions.InvalidEnumValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.koi151.payment.model.response.ErrorResponse;


import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerAdvisor {

    // ENUM EX ====================
    @ExceptionHandler(InvalidEnumValueException.class)
    public ResponseEntity<ErrorResponse> handleInvalidEnumValueException(InvalidEnumValueException ex) {

        List<String> details = new ArrayList<>();
        details.add("Recheck status value");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(ex.getMessage());
        errorResponse.setDetails(details);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // CONSTRAIN VIOLATION ===========================
    // throw when violate mysql validation
    @ExceptionHandler(ConstraintViolationException.class)
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

    // exception throw when violate jakarta validation
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
}