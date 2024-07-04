package com.example.msaccount.controllerAdvice;


import com.example.msaccount.customExceptions.*;
import com.example.msaccount.model.response.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.UnexpectedTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerAdvisor {

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp) {
//        var errors = new HashMap<>();
//        exp.getBindingResult().getAllErrors()
//                .forEach(error -> {
//                    var fieldName = ((FieldError) error).getField();
//                    var errorMessage = error.getDefaultMessage();
//                    errors.put(fieldName, errorMessage);
//                });
//
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body(new ErrorResponse(errors));
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

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("Validation Failed");
        errorResponse.setDetails(constraintViolations);

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(UnexpectedTypeException.class) // Occurs when enumerate value validation fails.
    public ResponseEntity<Object> handleUnexpectedTypeException(UnexpectedTypeException ex) {
        List<String> details = new ArrayList<>();
        details.add("Invalid enumeration values provided. Recheck type, houseDirection, balconyDirection or status");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(ex.getMessage());
        errorResponse.setDetails(details);

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(EmptyFileException.class)
    public ResponseEntity<Object> handleEmptyFileException(EmptyFileException ex) {
        List<String> details = new ArrayList<>();
        details.add("Images file is empty, recheck file value again");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("Validation Error");
        errorResponse.setDetails(details);

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(CloudinaryUploadFailedException.class)
    public ResponseEntity<Object> handleCloudinaryUploadFailedException(CloudinaryUploadFailedException ex) {
        List<String> details = new ArrayList<>();
        details.add("Unexpected error appeared in Cloudinary API, cannot upload image Cloudinary");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(ex.getMessage());
        errorResponse.setDetails(details);

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
    }

    @ExceptionHandler(PhoneAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handlePhoneAlreadyExistsException(PhoneAlreadyExistsException ex) {
        List<String> details = new ArrayList<>();
        details.add("Phone number already existed, please choose another one");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(ex.getMessage());
        errorResponse.setDetails(details);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(AccountAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserNameAlreadyExistsException(AccountAlreadyExistsException ex) {
        List<String> details = new ArrayList<>();
        details.add("User name already existed, please choose another one");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(ex.getMessage());
        errorResponse.setDetails(details);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotFoundException(AccountNotFoundException ex) {
        List<String> details = new ArrayList<>();
        details.add("Account not existed or might be deleted");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(ex.getMessage());
        errorResponse.setDetails(details);

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Object> handleRoleNotFoundException(RoleNotFoundException ex) {
        List<String> details = new ArrayList<>();
        details.add("Role not existed, recheck role id again");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(ex.getMessage());
        errorResponse.setDetails(details);

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<Object> handleUnauthorizedActionException(UnauthorizedActionException ex) {
        List<String> details = new ArrayList<>();
        details.add("Current account do not have permission to create new account.");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(ex.getMessage());
        errorResponse.setDetails(details);

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream() // create new stream
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList()); // convert to list

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("Validation failed");
        errorResponse.setDetails(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex) {
        List<String> details = new ArrayList<>();
        details.add("Wrong login information, recheck phone number or password");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(ex.getMessage());
        errorResponse.setDetails(details);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
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

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ErrorResponse> handlePasswordMismatchException(InvalidEnumValueException ex) {

        List<String> details = new ArrayList<>();
        details.add("Retype password does not match, please try again");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(ex.getMessage());
        errorResponse.setDetails(details);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}