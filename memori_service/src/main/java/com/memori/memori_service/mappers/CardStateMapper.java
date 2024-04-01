package com.memori.memori_service.mappers;

import org.springframework.stereotype.Component;

import com.memori.memori_domain.Card;

@Component
public class CardStateMapper {
    public Card.State asState(Integer value) {
        if (value == null)
            throw new IllegalArgumentException("Value cannot be null");
        return Card.State.fromValue(value);
    }

    public Integer asInteger(Card.State value) {
        return value.getValue();
    }
}
