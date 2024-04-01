package com.memori.memori_service.mappers;

import org.springframework.stereotype.Component;

import com.memori.memori_domain.DeckSettings;

@Component
public class DeckPriorityMapper {
    public DeckSettings.Priority asPriority(Integer value) {
        if (value == null)
            throw new IllegalArgumentException("Value cannot be null");
        return DeckSettings.Priority.fromValue(value);
    }

    public Integer asInteger(DeckSettings.Priority value) {
        return value.getValue();
    }
}