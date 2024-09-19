package com.example.msaccount.controllerAdvice;


import com.example.msaccount.customExceptions.*;
import com.example.msaccount.model.response.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.apache.tomcat.util.http.fileupload.impl.InvalidContentTypeException;
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

    //handleConstraintViolationException > MethodArgumentNotValidException > Binding...
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> constraintViolations = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        return ResponseEntity.badRequest()
            .body(ErrorResponse.builder()
                .error("Validation Failed")
                .details(constraintViolations)
                .build());
    }

    @ExceptionHandler(EmptyFileException.class)
    public ResponseEntity<Object> handleEmptyFileException(EmptyFileException ex) {
        List<String> details = new ArrayList<>();
        details.add("Images file is empty, recheck file value again");

        return ResponseEntity.badRequest()
            .body(ErrorResponse.builder()
                .error("Validation Error")
                .details(details)
                .build());
    }

    @ExceptionHandler(CloudinaryUploadFailedException.class)
    public ResponseEntity<Object> handleCloudinaryUploadFailedException(CloudinaryUploadFailedException ex) {
        List<String> details = new ArrayList<>();
        details.add("Unexpected error appeared in Cloudinary API, cannot upload image Cloudinary");

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(ErrorResponse.builder()
                .error(ex.getMessage())
                .details(details)
                .build());
    }

    @ExceptionHandler(PhoneAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handlePhoneAlreadyExistsException(PhoneAlreadyExistsException ex) {
        List<String> details = new ArrayList<>();
        details.add("Phone number already existed, please choose another one");

        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ErrorResponse.builder()
                .error(ex.getMessage())
                .details(details)
                .build());
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEntityAlreadyExistsException(EntityAlreadyExistsException ex) {
        List<String> details = new ArrayList<>();
        details.add("Entity already existed, cannot duplicate");

        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ErrorResponse.builder()
                .error(ex.getMessage())
                .details(details)
                .build());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        List<String> details = new ArrayList<>();
        details.add("Entity not existed or might be deleted");

        return new ResponseEntity<>(ErrorResponse.builder()
            .error(ex.getMessage())
            .details(details)
            .build()
        , HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<Object> handleUnauthorizedActionException(UnauthorizedActionException ex) {
        List<String> details = new ArrayList<>();
        details.add("Current account do not have permission to create new account.");

        return new ResponseEntity<>(ErrorResponse.builder()
            .error(ex.getMessage())
            .details(details)
            .build()
        , HttpStatus.FORBIDDEN);
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

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex) {
        List<String> details = new ArrayList<>();
        details.add("Wrong login information, recheck phone number or password");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponse.builder()
                .error(ex.getMessage())
                .details(details)
                .build());
    }

    @ExceptionHandler(InvalidEnumValueException.class)
    public ResponseEntity<ErrorResponse> handleInvalidEnumValueException(InvalidEnumValueException ex) {

        List<String> details = new ArrayList<>();
        details.add("Recheck spelling of enum");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.builder()
                .error(ex.getMessage())
                .details(details)
                .build());
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ErrorResponse> handlePasswordMismatchException(PasswordMismatchException ex) {

        List<String> details = new ArrayList<>();
        details.add("Retyped password does not match, please try again");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.builder()
                .error(ex.getMessage())
                .details(details)
                .build());
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<String> handleInvalidRequestException(InvalidRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // KEYCLOAK ==============
    @ExceptionHandler(KeycloakAccountCreationException.class)
    public ResponseEntity<ErrorResponse> handleKeycloakAccountCreationException(KeycloakAccountCreationException ex) {
        List<String> details = new ArrayList<>();
        details.add("Check the account creation request information before sending to Keycloak");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.builder()
                .error(ex.getMessage())
                .details(details)
                .build());
    }

    @ExceptionHandler(KeycloakRoleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleKeycloakRoleNotFoundException(KeycloakRoleNotFoundException ex) {
        List<String> details = new ArrayList<>();
        details.add("Check the role name in account creation request again");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.builder()
                .error(ex.getMessage())
                .details(details)
                .build());
    }

    @ExceptionHandler(KeycloakUserCreationException.class)
    public ResponseEntity<ErrorResponse> handleKeycloakUserCreationException(KeycloakUserCreationException ex) {
        List<String> details = new ArrayList<>();
        details.add("Recheck data sending to Keycloak");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.builder()
                .error(ex.getMessage())
                .details(details)
                .build());
    }

    @ExceptionHandler(KeycloakGroupNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleKeycloakGroupNotFoundException(KeycloakRoleNotFoundException ex) {
        List<String> details = new ArrayList<>();
        details.add("Check the group name again");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.builder()
                .error(ex.getMessage())
                .details(details)
                .build());
    }

    @ExceptionHandler(InvalidContentTypeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidContentTypeException(InvalidContentTypeException ex) {
        List<String> details = new ArrayList<>();
        details.add("Check the request body again");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.builder()
                .error(ex.getMessage())
                .details(details)
                .build());
    }
}