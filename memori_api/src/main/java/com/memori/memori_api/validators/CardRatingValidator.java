package com.memori.memori_api.validators;

import org.springframework.beans.factory.annotation.Autowired;

import com.memori.memori_service.services.ReviewLogService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CardRatingValidator implements ConstraintValidator<ValidCardRating, Integer> {

    @Autowired
    ReviewLogService reviewLogService;
    
    @Override
    public void initialize(ValidCardRating constraintAnnotation) {
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null)
            return true;
        return reviewLogService.isCardRatingValid(value);
    }
}