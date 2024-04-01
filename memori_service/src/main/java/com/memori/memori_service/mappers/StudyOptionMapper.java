package com.memori.memori_service.mappers;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.memori.memori_domain.StudyOption;
import com.memori.memori_service.dtos.StudyOptionDto;

@Mapper(config = SyncEntityMapperConfig.class)
public interface StudyOptionMapper extends SyncEntityMapper<StudyOptionDto, StudyOption> {
    @Override
    @InheritConfiguration(name = "dtoToEntityMapping")
    @Mapping(target = "studyOptionTags", ignore = true)
    @Mapping(target = "studyOptionStates", ignore = true)
    @Mapping(target = "studyOptionDecks", ignore = true)
    StudyOption dtoToEntity(StudyOptionDto dto);

    @Override
    @InheritInverseConfiguration(name = "dtoToEntity")
    @InheritConfiguration(name = "entityToDtoMapping")
    StudyOptionDto entityToDto(StudyOption entity);
}
