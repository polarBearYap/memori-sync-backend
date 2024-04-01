package com.memori.memori_service.mappers;

import org.springframework.stereotype.Component;

import com.memori.memori_domain.StudyOption;

@Component
public class StudySortOptionMapper {
    public StudyOption.SortOption asSortOption(Integer value) {
        if (value == null)
            throw new IllegalArgumentException("Value cannot be null");
        return StudyOption.SortOption.fromValue(value);
    }

    public Integer asInteger(StudyOption.SortOption value) {
        return value.getValue();
    }
}