package com.memori.memori_data.mappers;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.memori.memori_domain.StudyOption;

@Mapper(config = EntityMapperConfig.class)
public interface StudyOptionEntityMapper extends EntityMapper<StudyOption> {
    @Override
    void mapEntity(StudyOption source, @MappingTarget StudyOption target);

    @Override
    @InheritConfiguration(name = "updateEntity")
    void updateEntity(StudyOption source, @MappingTarget StudyOption target);

    @Override
    @InheritConfiguration(name = "deleteEntity")
    void deleteEntity(StudyOption source, @MappingTarget StudyOption target);
}
