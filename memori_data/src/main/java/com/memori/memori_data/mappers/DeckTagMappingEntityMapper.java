package com.memori.memori_data.mappers;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.memori.memori_domain.DeckTagMapping;

@Mapper(config = EntityMapperConfig.class)
public interface DeckTagMappingEntityMapper extends EntityMapper<DeckTagMapping> {
    @Override
    void mapEntity(DeckTagMapping source, @MappingTarget DeckTagMapping target);

    @Override
    @InheritConfiguration(name = "updateEntity")
    void updateEntity(DeckTagMapping source, @MappingTarget DeckTagMapping target);

    @Override
    @InheritConfiguration(name = "deleteEntity")
    void deleteEntity(DeckTagMapping source, @MappingTarget DeckTagMapping target);
}
