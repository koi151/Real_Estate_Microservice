package com.koi151.msproperties.controllerAdvice;

import com.koi151.msproperties.dto.ErrorResponseDTO;
import customExceptions.PropertyNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
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

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorsMap = new HashMap<>();
        errorsMap.put("errors", errors);
        return errorsMap;
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
