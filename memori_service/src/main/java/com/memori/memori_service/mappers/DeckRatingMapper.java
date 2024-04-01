package com.memori.memori_service.mappers;

import org.springframework.stereotype.Component;

import com.memori.memori_domain.DeckReviews;

@Component
public class DeckRatingMapper {
    public DeckReviews.Rating asRating(Integer value) {
        if (value == null)
            throw new IllegalArgumentException("Value cannot be null");
        return DeckReviews.Rating.fromValue(value);
    }

    public Integer asInteger(DeckReviews.Rating value) {
        return value.getValue();
    }
}

