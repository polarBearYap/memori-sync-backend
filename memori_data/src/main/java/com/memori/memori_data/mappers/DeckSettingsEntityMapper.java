package com.memori.memori_data.mappers;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.memori.memori_domain.DeckSettings;

@Mapper(config = EntityMapperConfig.class)
public interface DeckSettingsEntityMapper extends EntityMapper<DeckSettings> {
    @Override
    void mapEntity(DeckSettings source, @MappingTarget DeckSettings target);

    @Override
    @InheritConfiguration(name = "updateEntity")
    void updateEntity(DeckSettings source, @MappingTarget DeckSettings target);

    @Override
    @InheritConfiguration(name = "deleteEntity")
    void deleteEntity(DeckSettings source, @MappingTarget DeckSettings target);
}