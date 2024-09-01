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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerAdvisor {

    // ENUM EX ====================
    @ExceptionHandler(InvalidEnumValueException.class)
    public ResponseEntity<ErrorResponse> handleInvalidEnumValueException(InvalidEnumValueException ex) {
        return new ResponseEntity<>(ErrorResponse.builder()
            .error(ex.getMessage())
            .details(Collections.singletonList("Enum value not legit, recheck again")) // create unmodifiable List
            .build()
            , HttpStatus.BAD_REQUEST);
    }

    // CONSTRAIN VIOLATION ===========================
    // throw when violate mysql validation
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        Throwable cause = ex.getCause();

        if (cause instanceof SQLIntegrityConstraintViolationException sqlEx) {
            String message = cause.getMessage();
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.builder()
                    .error("Duplicate entry: ")
                    .details(Collections.singletonList(message))
                    .build());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.builder()
                .error(ex.getMessage())
                .details(Collections.singletonList("Constraint violated, recheck again"))
                .build();
    }

    // exception throw when violate jakarta validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        List<String> details = ex.getBindingResult().getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.builder()
                .error("Validation failed")
                .details(details)
                .build());
    }
}