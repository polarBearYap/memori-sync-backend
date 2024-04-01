package com.memori.memori_data.mappers;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.memori.memori_domain.StudyOptionState;

@Mapper(config = EntityMapperConfig.class)
public interface StudyOptionStateEntityMapper extends EntityMapper<StudyOptionState> {
    @Override
    void mapEntity(StudyOptionState source, @MappingTarget StudyOptionState target);

    @Override
    @InheritConfiguration(name = "updateEntity")
    void updateEntity(StudyOptionState source, @MappingTarget StudyOptionState target);

    @Override
    @InheritConfiguration(name = "deleteEntity")
    void deleteEntity(StudyOptionState source, @MappingTarget StudyOptionState target);
}
