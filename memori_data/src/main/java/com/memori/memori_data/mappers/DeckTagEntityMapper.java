package com.memori.memori_data.mappers;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.memori.memori_domain.DeckTag;

@Mapper(config = EntityMapperConfig.class)
public interface DeckTagEntityMapper extends EntityMapper<DeckTag> {
    @Override
    void mapEntity(DeckTag source, @MappingTarget DeckTag target);

    @Override
    @InheritConfiguration(name = "updateEntity")
    void updateEntity(DeckTag source, @MappingTarget DeckTag target);

    @Override
    @InheritConfiguration(name = "deleteEntity")
    void deleteEntity(DeckTag source, @MappingTarget DeckTag target);
}
