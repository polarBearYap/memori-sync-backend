package com.memori.memori_data.mappers;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.memori.memori_domain.DeckImage;

@Mapper(config = EntityMapperConfig.class)
public interface DeckImageEntityMapper extends EntityMapper<DeckImage> {
    @Override
    void mapEntity(DeckImage source, @MappingTarget DeckImage target);

    @Override
    @InheritConfiguration(name = "updateEntity")
    void updateEntity(DeckImage source, @MappingTarget DeckImage target);

    @Override
    @InheritConfiguration(name = "deleteEntity")
    void deleteEntity(DeckImage source, @MappingTarget DeckImage target);
}
