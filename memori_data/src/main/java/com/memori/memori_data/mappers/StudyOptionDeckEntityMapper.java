package com.memori.memori_data.mappers;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.memori.memori_domain.StudyOptionDeck;

@Mapper(config = EntityMapperConfig.class)
public interface StudyOptionDeckEntityMapper extends EntityMapper<StudyOptionDeck> {
    @Override
    void mapEntity(StudyOptionDeck source, @MappingTarget StudyOptionDeck target);

    @Override
    @InheritConfiguration(name = "updateEntity")
    void updateEntity(StudyOptionDeck source, @MappingTarget StudyOptionDeck target);

    @Override
    @InheritConfiguration(name = "deleteEntity")
    void deleteEntity(StudyOptionDeck source, @MappingTarget StudyOptionDeck target);
}
