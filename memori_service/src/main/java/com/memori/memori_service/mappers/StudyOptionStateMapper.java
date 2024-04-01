package com.memori.memori_service.mappers;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.memori.memori_domain.StudyOptionState;
import com.memori.memori_service.dtos.StudyOptionStateDto;

@Mapper(config = SyncEntityMapperConfig.class)
public interface StudyOptionStateMapper extends SyncEntityMapper<StudyOptionStateDto, StudyOptionState> {
    @Override
    @InheritConfiguration(name = "dtoToEntityMapping")
    @Mapping(target = "studyOption", ignore = true)
    StudyOptionState dtoToEntity(StudyOptionStateDto dto);

    @Override
    @InheritInverseConfiguration(name = "dtoToEntity")
    @InheritConfiguration(name = "entityToDtoMapping")
    StudyOptionStateDto entityToDto(StudyOptionState entity);
}