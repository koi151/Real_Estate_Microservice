package com.example.msaccount.controllerAdvice;

import com.example.msaccount.dto.ErrorResponseDTO;
import customExceptions.CloudinaryUploadFailedException;
import customExceptions.PhoneAlreadyExistsException;
import customExceptions.EmptyFileException;
import customExceptions.UserNameAlreadyExistsException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.UnexpectedTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerAdvisor {

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
//
//        List<String> errors = ex.getBindingResult().getFieldErrors()
//                .stream() // create new stream
//                .map(FieldError::getDefaultMessage)
//                .collect(Collectors.toList()); // convert to list
//
//        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
//        errorResponseDTO.setError("Invalid request");
//        errorResponseDTO.setDetails(errors);
//
//        return ResponseEntity.badRequest().body(errorResponseDTO);
//    }

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

    @ExceptionHandler(UnexpectedTypeException.class) // Occurs when enumerate value validation fails.
    public ResponseEntity<Object> handleUnexpectedTypeException(UnexpectedTypeException ex) {
        List<String> details = new ArrayList<>();
        details.add("Invalid enumeration values provided. Recheck type, houseDirection, balconyDirection or status");

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setError(ex.getMessage());
        errorResponseDTO.setDetails(details);

        return ResponseEntity.badRequest().body(errorResponseDTO);
    }

    @ExceptionHandler(EmptyFileException.class)
    public ResponseEntity<Object> handleEmptyFileException(EmptyFileException ex) {
        List<String> details = new ArrayList<>();
        details.add("Images file is empty, recheck file value again");

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setError("Validation Error");
        errorResponseDTO.setDetails(details);

        return ResponseEntity.badRequest().body(errorResponseDTO);
    }

    @ExceptionHandler(CloudinaryUploadFailedException.class)
    public ResponseEntity<Object> handleCloudinaryUploadFailedException(CloudinaryUploadFailedException ex) {
        List<String> details = new ArrayList<>();
        details.add("Unexpected error appeared in Cloudinary API, cannot upload image Cloudinary");

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setError(ex.getMessage());
        errorResponseDTO.setDetails(details);

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponseDTO);
    }

    @ExceptionHandler(PhoneAlreadyExistsException.class)
    public ResponseEntity<Object> handlePhoneAlreadyExistsException(PhoneAlreadyExistsException ex) {
        List<String> details = new ArrayList<>();
        details.add("Phone number already existed, please choose another one");

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setError(ex.getMessage());
        errorResponseDTO.setDetails(details);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDTO);
    }

    @ExceptionHandler(UserNameAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserNameAlreadyExistsException(UserNameAlreadyExistsException ex) {
        List<String> details = new ArrayList<>();
        details.add("User name already existed, please choose another one");

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setError(ex.getMessage());
        errorResponseDTO.setDetails(details);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDTO);
    }
}
