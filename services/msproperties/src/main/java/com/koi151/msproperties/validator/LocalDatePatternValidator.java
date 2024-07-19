package com.koi151.msproperties.validator;

import com.koi151.msproperties.annotations.LocalDatePattern;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDatePatternValidator implements ConstraintValidator<LocalDatePattern, LocalDate> {
    private String pattern;

    @Override
    public void initialize(LocalDatePattern constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Null values are considered valid
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDate.parse(value.toString(), formatter);
            return true; // Parsing succeeded, valid format
        } catch (DateTimeParseException e) {
            return false; // Parsing failed, invalid format
        }
    }
}
