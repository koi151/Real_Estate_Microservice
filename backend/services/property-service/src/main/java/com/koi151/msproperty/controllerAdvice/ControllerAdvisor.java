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
import org.springframework.web.context.request.WebRequest;

import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(PropertyNotFoundException.class)
    public ResponseEntity<Object> handlePropertyNotFoundException(PropertyNotFoundException ex, WebRequest request) {
        ErrorResponse errorResponseDTO = new ErrorResponse();
        errorResponseDTO.setError(ex.getMessage());

        List<String> details = new ArrayList<>();
        details.add("Property not existed or might be deleted");
        errorResponseDTO.setDetails(details);

        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PaymentScheduleNotFoundException.class)
    public ResponseEntity<Object> handlePaymentScheduleNotFoundException(PaymentScheduleNotFoundException ex) {
        ErrorResponse errorResponseDTO = new ErrorResponse();
        errorResponseDTO.setError(ex.getMessage());

        List<String> details = new ArrayList<>();
        details.add("Please add valid payment schedule for property");
        errorResponseDTO.setDetails(details);

        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_GATEWAY);
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

    //handleConstraintViolationException > MethodArgumentNotValidException > Binding...
    // The below error type occurred when violated @NotNull validation
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(
            ConstraintViolationException ex) {

        List<String> constraintViolations = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        ErrorResponse errorResponseDTO = new ErrorResponse();
        errorResponseDTO.setError("Validation errors occurred");
        errorResponseDTO.setDetails(constraintViolations);

        return ResponseEntity.badRequest().body(errorResponseDTO);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Object> handleCategoryNotFoundException
            (CategoryNotFoundException ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(ex.getMessage());
        List<String> details = new ArrayList<>();
        details.add("Property category not existed or might be deleted");
        errorResponse.setDetails(details);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(UnexpectedTypeException.class) // Occurs when enumerate value validation fails.
    public ResponseEntity<Object> handleUnexpectedTypeException(UnexpectedTypeException ex) {
        List<String> details = new ArrayList<>();
        details.add("UnexpectedTypeException occurred");

        ErrorResponse errorResponseDTO = new ErrorResponse();
        errorResponseDTO.setError(ex.getMessage());
        errorResponseDTO.setDetails(details);

        return ResponseEntity.badRequest().body(errorResponseDTO);
    }

    @ExceptionHandler(EmptyFileException.class)
    public ResponseEntity<Object> handleEmptyFileException(EmptyFileException ex) {
        List<String> details = new ArrayList<>();
        details.add("Images file is empty, recheck file value again");

        ErrorResponse errorResponseDTO = new ErrorResponse();
        errorResponseDTO.setError(ex.getMessage());
        errorResponseDTO.setDetails(details);

        return ResponseEntity.badRequest().body(errorResponseDTO);
    }

    @ExceptionHandler(MaxImagesExceededException.class)
    public ResponseEntity<Object> handleMaxImagesExceededException(MaxImagesExceededException ex) {
        List<String> details = new ArrayList<>();
        details.add("Maximum 8 images allow reached, cannot add more images");

        ErrorResponse errorResponseDTO = new ErrorResponse();
        errorResponseDTO.setError(ex.getMessage());
        errorResponseDTO.setDetails(details);

        return ResponseEntity.badRequest().body(errorResponseDTO);
    }

    @ExceptionHandler(InvalidContentTypeException.class)
    public ResponseEntity<Object> handleInvalidContentTypeException(InvalidContentTypeException ex) {
        List<String> details = new ArrayList<>();
        details.add("Invalid Content-Type value, recheck again.");

        ErrorResponse errorResponseDTO = new ErrorResponse();
        errorResponseDTO.setError(ex.getMessage());
        errorResponseDTO.setDetails(details);

        return ResponseEntity.badRequest().body(errorResponseDTO);
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

    @ExceptionHandler(DateTimeException.class) // for validation and other purposes
    public ResponseEntity<ErrorResponse> handleDateTimeException(DateTimeException ex) {

        List<String> details = new ArrayList<>();
        details.add("Recheck date time in request");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(ex.getMessage());
        errorResponse.setDetails(details);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
