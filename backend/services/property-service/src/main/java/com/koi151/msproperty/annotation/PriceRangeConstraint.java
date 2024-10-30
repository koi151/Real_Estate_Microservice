package com.koi151.msproperty.annotation;


import com.koi151.msproperty.validator.PriceRangeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// ElementType.TYPE: Target the type (classes including records, interface)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME) // annotation should be retained at runtime, processing during the application's execution.
@Constraint(validatedBy = PriceRangeValidator.class) // links the annotation to its corresponding validator class
public @interface PriceRangeConstraint {

    // default error message displayed in case of validation fails
    String message() default "Invalid price range: 'priceFrom' value must be less than or equal to 'priceTo'";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
