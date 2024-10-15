package com.koi151.notification.controllerAdvice;

import com.koi151.notification.customExceptions.InvalidEnumValueException;
import com.koi151.notification.model.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;

@ControllerAdvice
public class ControllerAdvisor {

    // ENUM EX =================
    @ExceptionHandler(InvalidEnumValueException.class)
    public ResponseEntity<ErrorResponse> handleInvalidEnumValueException(InvalidEnumValueException ex) {
        return new ResponseEntity<>(ErrorResponse.builder()
            .error(ex.getMessage())
            .details(Collections.singletonList("Enum value not legit, recheck again")) // create unmodifiable List
            .build()
        , HttpStatus.BAD_REQUEST);
    }
}
