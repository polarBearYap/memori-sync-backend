package com.memori.memori_service.mappers;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.memori.memori_domain.StudyOptionDeck;
import com.memori.memori_service.dtos.StudyOptionDeckDto;

@Mapper(config = SyncEntityMapperConfig.class)
public interface StudyOptionDeckMapper extends SyncEntityMapper<StudyOptionDeckDto, StudyOptionDeck> {
    @Override
    @InheritConfiguration(name = "dtoToEntityMapping")
    @Mapping(target = "deck", ignore = true)
    @Mapping(target = "studyOption", ignore = true)
    StudyOptionDeck dtoToEntity(StudyOptionDeckDto dto);

    @Override
    @InheritInverseConfiguration(name = "dtoToEntity")
    @InheritConfiguration(name = "entityToDtoMapping")
    StudyOptionDeckDto entityToDto(StudyOptionDeck entity);
}
