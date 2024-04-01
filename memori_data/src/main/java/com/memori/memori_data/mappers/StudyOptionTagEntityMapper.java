package com.memori.memori_data.mappers;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.memori.memori_domain.StudyOptionTag;

@Mapper(config = EntityMapperConfig.class)
public interface StudyOptionTagEntityMapper extends EntityMapper<StudyOptionTag> {
    @Override
    void mapEntity(StudyOptionTag source, @MappingTarget StudyOptionTag target);

    @Override
    @InheritConfiguration(name = "updateEntity")
    void updateEntity(StudyOptionTag source, @MappingTarget StudyOptionTag target);

    @Override
    @InheritConfiguration(name = "deleteEntity")
    void deleteEntity(StudyOptionTag source, @MappingTarget StudyOptionTag target);
}
