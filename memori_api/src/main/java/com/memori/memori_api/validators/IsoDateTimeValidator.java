package com.memori.memori_api.validators;

import org.springframework.beans.factory.annotation.Autowired;

import com.memori.memori_service.mappers.DateTimeMapper;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsoDateTimeValidator implements ConstraintValidator<ValidIsoDateTime, String> {
    
    @Autowired
    DateTimeMapper dateTimeMapper;

    @Override
    public void initialize(ValidIsoDateTime constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null)
            return true;
        try {
            dateTimeMapper.asInstant(value);
            return true;
        }
        catch (Exception ex) {
        }
        try {
            dateTimeMapper.asOffsetDateTime(value);
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }
}
