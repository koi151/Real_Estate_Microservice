package com.koi151.listing_services.controllerAdvice;

import com.koi151.listing_services.customExceptions.*;
import com.koi151.listing_services.model.response.ErrorResponse;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(InvalidEnumValueException.class)
    public ResponseEntity<ErrorResponse> handleInvalidEnumValueException(InvalidEnumValueException ex) {

        List<String> details = new ArrayList<>();
        details.add("Recheck status value");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(ex.getMessage());
        errorResponse.setDetails(details);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
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

    // CONSTRAIN VIOLATION //
    @ExceptionHandler(ConstraintViolationException.class) // using for common case
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        Throwable cause = ex.getCause();
        ErrorResponse errorResponse = new ErrorResponse();

        if (cause instanceof SQLIntegrityConstraintViolationException sqlEx) {
            String message = cause.getMessage();

            List<String> details = new ArrayList<>();
            details.add(message);

            errorResponse.setError("Data validate failed: ");
            errorResponse.setDetails(details);
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    // Entity not found ex

    @ExceptionHandler(EntityNotFoundCustomException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundCustomException(EntityNotFoundCustomException ex) {
        List<String> details = new ArrayList<>();
        details.add("Entity not exists, recheck again");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setDetails(details);
        errorResponse.setError(ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    // DUPLICATE ENTITY ==============================================
    @ExceptionHandler({ DuplicatePostServiceException.class, DuplicatePostServiceCategoryException.class, DuplicatedPropertyPostPackageException.class })
    public ResponseEntity<ErrorResponse> handleDuplicateException(RuntimeException ex) {

        List<String> details = new ArrayList<>();
        StringBuilder detailMessage = getDetailMessage(ex);
        details.add(String.valueOf(detailMessage));

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setDetails(details);
        errorResponse.setError(ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    // VALIDATION FAILED EX ===========================================
    private static StringBuilder getDetailMessage(RuntimeException ex) {
        StringBuilder detailMessage = new StringBuilder();

        if (ex instanceof DuplicatePostServiceException) {
            detailMessage.append("Post service");
        } else if (ex instanceof DuplicatePostServiceCategoryException) {
            detailMessage.append("Post service category");
        } else if (ex instanceof DuplicatedPropertyPostPackageException){
            detailMessage.append("Post service property post package");
        }

        detailMessage.append(" validate failed, please recheck again");
        return detailMessage;
    }

    @ExceptionHandler(ValidationFailedException.class)
    public ResponseEntity<ErrorResponse> handleValidationFailedException(ValidationFailedException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("Validation failed");
        errorResponse.setDetails(details);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    // NOT EXISTS EX ==================================
    @ExceptionHandler(PostServiceNotExistedException.class)
    public ResponseEntity<ErrorResponse> handlePostServiceNotExistedException(PostServiceNotExistedException ex) {
        List<String> details = new ArrayList<>();
        details.add("Post service not existed, recheck again");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setDetails(details);
        errorResponse.setError(ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }


    // OTHER SERVICES UNAVAILABLE EX ================================
    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleServiceUnavailableException(ServiceUnavailableException ex) {
        List<String> details = new ArrayList<>();
        details.add("Service is unavailable now, recheck if error occurred or service is not working");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setDetails(details);
        errorResponse.setError(ex.getMessage());

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
    }

    // DESERIALIZE EX
    @ExceptionHandler(FailedToDeserializingData.class)
    public ResponseEntity<ErrorResponse> handleFailedToDeserializingData(FailedToDeserializingData ex) {
        List<String> details = new ArrayList<>();
        details.add("Failed to deserialize data, recheck again");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setDetails(details);
        errorResponse.setError(ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}

