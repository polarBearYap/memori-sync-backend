package com.memori.memori_service.mappers;

import org.springframework.stereotype.Component;

import com.memori.memori_domain.StudyOption;

@Component
public class StudyOptionModeMapper {
    public StudyOption.Mode asMode(Integer value) {
        if (value == null)
            throw new IllegalArgumentException("Value cannot be null");
        return StudyOption.Mode.fromValue(value);
    }

    public Integer asInteger(StudyOption.Mode value) {
        return value.getValue();
    }
}