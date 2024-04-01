package com.memori.memori_api.validators;

import org.springframework.beans.factory.annotation.Autowired;

import com.memori.memori_service.services.CardService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CardStateValidator implements ConstraintValidator<ValidCardState, Integer> {

    @Autowired
    CardService cardService;
    
    @Override
    public void initialize(ValidCardState constraintAnnotation) {
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null)
            return true;
        return cardService.isCardStateValid(value);
    }
}