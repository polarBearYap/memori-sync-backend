package com.memori.memori_service.mappers;

import org.springframework.stereotype.Component;

import com.memori.memori_domain.ReviewLog;

@Component
public class CardRatingMapper {
    public ReviewLog.Rating asRating(Integer value) {
        if (value == null)
            throw new IllegalArgumentException("Value cannot be null");
        return ReviewLog.Rating.fromValue(value);
    }

    public Integer asInteger(ReviewLog.Rating value) {
        return value.getValue();
    }
}