package com.koi151.msproperties.controllerAdvice;

import com.koi151.msproperties.dto.ErrorResponseDTO;
import customExceptions.PaymentScheduleNotFoundException;
import customExceptions.PropertyNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.UnexpectedTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(PropertyNotFoundException.class)
    public ResponseEntity<Object> handlePropertyNotFoundException(PropertyNotFoundException ex, WebRequest request) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setError(ex.getMessage());

        List<String> details = new ArrayList<>();
        details.add("Cannot find property");
        errorResponseDTO.setDetails(details);

        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(PaymentScheduleNotFoundException.class)
    public ResponseEntity<Object> handlePaymentScheduleNotFoundException(PaymentScheduleNotFoundException ex) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
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

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setError("Invalid request");
        errorResponseDTO.setDetails(errors);

        return ResponseEntity.badRequest().body(errorResponseDTO);
    }

    //handleConstraintViolationException > MethodArgumentNotValidException > Binding...
    // The below error type occurred when violated @NotNull validate
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(
            ConstraintViolationException ex) {

        List<String> constraintViolations = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setError("Validation Failed");
        errorResponseDTO.setDetails(constraintViolations);

        return ResponseEntity.badRequest().body(errorResponseDTO);
    }

//    @ExceptionHandler(UnexpectedTypeException.class) // Occurs when enumerate value validation fails.
//    public ResponseEntity<Object> handleUnexpectedTypeException(UnexpectedTypeException ex) {
//        List<String> details = new ArrayList<>();
//        details.add("Invalid enumeration values provided. Recheck type, houseDirection, balconyDirection or status");
//
//        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
//        errorResponseDTO.setError("Validation Error");
//        errorResponseDTO.setDetails(details);
//
//        return ResponseEntity.badRequest().body(errorResponseDTO);
//    }
}
