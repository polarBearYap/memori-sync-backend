package com.memori.memori_api.validators;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = IsoDateTimeValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidIsoDateTime {
    String message() default "Invalid ISO 8601 date format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
