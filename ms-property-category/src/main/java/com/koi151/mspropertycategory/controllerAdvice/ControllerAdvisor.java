package com.koi151.mspropertycategory.controllerAdvice;

import com.koi151.mspropertycategory.dto.ErrorResponseDTO;
import customExceptions.CategoryNotFoundException;
import customExceptions.FieldRequiredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerAdvisor {
    @ExceptionHandler(FieldRequiredException.class)
    public ResponseEntity<Object> handleFieldRequiredException
            (FieldRequiredException ex, WebRequest request) {

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setError(ex.getMessage());
        List<String> details = new ArrayList<>();
        details.add("Recheck title again, it's return null!");
        errorResponseDTO.setDetails(details);

        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Object> handleCategoryNotFoundException
            (CategoryNotFoundException ex, WebRequest request) {

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setError(ex.getMessage());
        List<String> details = new ArrayList<>();
        details.add("Property category not found");
        errorResponseDTO.setDetails(details);

        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleBindException(MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream() // create new stream
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList()); // convert to list

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setError("Invalid request");
        errorResponseDTO.setDetails(errors);

        return ResponseEntity.badRequest().body(errorResponseDTO);
    }
}
