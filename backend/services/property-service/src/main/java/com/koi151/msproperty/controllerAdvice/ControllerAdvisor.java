package com.koi151.msproperty.controllerAdvice;

import com.koi151.msproperty.customExceptions.*;
import com.koi151.msproperty.model.response.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.UnexpectedTypeException;
import org.apache.tomcat.util.http.fileupload.impl.InvalidContentTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(PropertyNotFoundException.class)
    public ResponseEntity<Object> handlePropertyNotFoundException(PropertyNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse.builder()
                .error(ex.getMessage())
                .details(List.of("Property not existed or might be deleted"))
                .build());
    }

    @ExceptionHandler(PaymentScheduleNotFoundException.class)
    public ResponseEntity<Object> handlePaymentScheduleNotFoundException(PaymentScheduleNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
            .body(ErrorResponse.builder()
                .error(ex.getMessage())
                .details(List.of("Please add valid payment schedule for property"))
                .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.builder()
                .error("Validation failed")
                .details(errors)
                .build());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> constraintViolations = ex.getConstraintViolations()
            .stream()
            .map(ConstraintViolation::getMessage)
            .collect(Collectors.toList());

        return ResponseEntity.badRequest()
            .body(ErrorResponse.builder()
                .error("Validation errors occurred")
                .details(constraintViolations)
                .build());
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Object> handleCategoryNotFoundException(CategoryNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
            .body(ErrorResponse.builder()
                .error(ex.getMessage())
                .details(List.of("Property category not existed or might be deleted"))
                .build());
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<Object> handleUnexpectedTypeException(UnexpectedTypeException ex) {
        return ResponseEntity.badRequest()
            .body(ErrorResponse.builder()
                .error(ex.getMessage())
                .details(List.of("UnexpectedTypeException occurred"))
                .build());
    }

    @ExceptionHandler(EmptyFileException.class)
    public ResponseEntity<Object> handleEmptyFileException(EmptyFileException ex) {
        return ResponseEntity.badRequest()
            .body(ErrorResponse.builder()
                .error(ex.getMessage())
                .details(List.of("Images file is empty, recheck file value again"))
                .build());
    }

    @ExceptionHandler(MaxImagesExceededException.class)
    public ResponseEntity<Object> handleMaxImagesExceededException(MaxImagesExceededException ex) {
        return ResponseEntity.badRequest()
            .body(ErrorResponse.builder()
                .error(ex.getMessage())
                .details(List.of("Maximum 8 images allow reached, cannot add more images"))
                .build());
    }

    @ExceptionHandler(InvalidContentTypeException.class)
    public ResponseEntity<Object> handleInvalidContentTypeException(InvalidContentTypeException ex) {
        return ResponseEntity.badRequest()
            .body(ErrorResponse.builder()
                .error(ex.getMessage())
                .details(List.of("Invalid Content-Type value, recheck again."))
                .build());
    }

    @ExceptionHandler(InvalidEnumValueException.class)
    public ResponseEntity<ErrorResponse> handleInvalidEnumValueException(InvalidEnumValueException ex) {
        return ResponseEntity.badRequest()
            .body(ErrorResponse.builder()
                .error(ex.getMessage())
                .details(List.of("Recheck status value"))
                .build());
    }

    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<ErrorResponse> handleDateTimeException(DateTimeException ex) {
        return ResponseEntity.badRequest()
            .body(ErrorResponse.builder()
                .error(ex.getMessage())
                .details(List.of("Recheck date time in request"))
                .build());
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestException(InvalidRequestException ex) {
        return ResponseEntity.badRequest()
            .body(ErrorResponse.builder()
                .error(ex.getMessage())
                .build());
    }
}
