package com.example.msaccount.controllerAdvice;


import com.example.msaccount.customExceptions.*;
import com.example.msaccount.model.response.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import org.apache.tomcat.util.http.fileupload.impl.InvalidContentTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
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
        return ResponseEntity.badRequest()
            .body(ErrorResponse.builder()
                .error("Validation Error")
                .details(Collections.singletonList("Images file is empty, recheck file value again"))
                .build());
    }

    @ExceptionHandler(CloudinaryUploadFailedException.class)
    public ResponseEntity<Object> handleCloudinaryUploadFailedException(CloudinaryUploadFailedException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(ErrorResponse.builder()
                .error(ex.getMessage())
                .details(Collections.singletonList("Unexpected error appeared in Cloudinary API, cannot upload image Cloudinary"))
                .build());
    }

    @ExceptionHandler(PhoneAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handlePhoneAlreadyExistsException(PhoneAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ErrorResponse.builder()
                .error(ex.getMessage())
                .details(Collections.singletonList("Phone number already existed, please choose another one"))
                .build());
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEntityAlreadyExistsException(EntityAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ErrorResponse.builder()
                .error(ex.getMessage())
                .details(Collections.singletonList("Entity already existed, cannot duplicate"))
                .build());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(ErrorResponse.builder()
            .error(ex.getMessage())
            .details(Collections.singletonList("Entity not existed or might be deleted"))
            .build()
        , HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<Object> handleUnauthorizedActionException(UnauthorizedActionException ex) {
        return new ResponseEntity<>(ErrorResponse.builder()
            .error(ex.getMessage())
            .details(Collections.singletonList("Current account do not have permission to create new account."))
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
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponse.builder()
                .error(ex.getMessage())
                .details(Collections.singletonList("Wrong login information, recheck phone number or password"))
                .build());
    }

    @ExceptionHandler(InvalidEnumValueException.class)
    public ResponseEntity<ErrorResponse> handleInvalidEnumValueException(InvalidEnumValueException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.builder()
                .error(ex.getMessage())
                .details(Collections.singletonList("Recheck spelling of enum"))
                .build());
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ErrorResponse> handlePasswordMismatchException(PasswordMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.builder()
                .error(ex.getMessage())
                .details(Collections.singletonList("Retyped password does not match, please try again"))
                .build());
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<String> handleInvalidRequestException(InvalidRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ex.getMessage());
    }

    // KEYCLOAK ==============
    @ExceptionHandler(KeycloakAccountCreationException.class)
    public ResponseEntity<ErrorResponse> handleKeycloakAccountCreationException(KeycloakAccountCreationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.builder()
                .error(ex.getMessage())
                .details(Collections.singletonList("Check the account creation request information before sending to Keycloak"))
                .build());
    }

    @ExceptionHandler(KeycloakUserUpdateException.class)
    public ResponseEntity<ErrorResponse> handleKeycloakUserUpdateException(KeycloakUserUpdateException ex) {
        String errorMessage = "Bad request";
        String errorDetails;

        Throwable cause = ex.getCause();
        if (cause instanceof BadRequestException) {
            Response response = ((BadRequestException) cause).getResponse();
            if (response != null && response.hasEntity()) {
                try {
                    errorDetails = response.readEntity(String.class);
                } catch (Exception e) {
                    log.error("Failed to read error details from BadRequestException", e);
                    errorDetails = "Failed to read error details.";
                }
            } else {
                errorDetails = cause.getMessage();
            }
        } else {
            errorDetails = ex.getMessage();
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
            .error(errorMessage)
            .details(Collections.singletonList(errorDetails))
            .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(errorResponse);
    }

    @ExceptionHandler(KeycloakRoleRetrievalException.class)
    public ResponseEntity<ErrorResponse> handleKeycloakRoleRetrievalException(KeycloakRoleRetrievalException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse.builder()
                .error("Failed to retrieve roles of user")
                .details(Collections.singletonList(ex.getMessage()))
                .build());
    }

    @ExceptionHandler(KeycloakResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleKeycloakResourceNotFoundException(KeycloakResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse.builder()
                .error("Keycloak resource not found")
                .details(Collections.singletonList(ex.getMessage()))
                .build());
    }

    @ExceptionHandler(KeycloakLoginFailedException.class)
    public ResponseEntity<ErrorResponse> handleKeycloakLoginFailedException(KeycloakLoginFailedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponse.builder()
                .error("Keycloak login failed")
                .details(Collections.singletonList(ex.getMessage()))
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
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.builder()
                .error("Failed to create Keycloak account")
                .details(Collections.singletonList(ex.getMessage()))
                .build());
    }

    @ExceptionHandler(KeycloakGroupNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleKeycloakGroupNotFoundException(KeycloakRoleNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.builder()
                .error(ex.getMessage())
                .details(Collections.singletonList("Check the group name again"))
                .build());
    }

    @ExceptionHandler(InvalidContentTypeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidContentTypeException(InvalidContentTypeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.builder()
                .error(ex.getMessage())
                .details(Collections.singletonList("Check the request body again"))
                .build());
    }
}