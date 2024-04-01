package com.memori.memori_service.mappers;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.memori.memori_domain.StudyOptionTag;
import com.memori.memori_service.dtos.StudyOptionTagDto;

@Mapper(config = SyncEntityMapperConfig.class)
public interface StudyOptionTagMapper extends SyncEntityMapper<StudyOptionTagDto, StudyOptionTag> {
    @Override
    @InheritConfiguration(name = "dtoToEntityMapping")
    @Mapping(target = "cardTag", ignore = true)
    @Mapping(target = "studyOption", ignore = true)
    StudyOptionTag dtoToEntity(StudyOptionTagDto dto);

    @Override
    @InheritInverseConfiguration(name = "dtoToEntity")
    @InheritConfiguration(name = "entityToDtoMapping")
    StudyOptionTagDto entityToDto(StudyOptionTag entity);
}