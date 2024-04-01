package com.memori.memori_data.mappers;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.memori.memori_domain.Deck;

@Mapper(config = EntityMapperConfig.class)
public interface DeckEntityMapper extends EntityMapper<Deck> {
    @Override
    void mapEntity(Deck source, @MappingTarget Deck target);

    @Override
    @InheritConfiguration(name = "updateEntity")
    void updateEntity(Deck source, @MappingTarget Deck target);

    @Override
    @InheritConfiguration(name = "deleteEntity")
    void deleteEntity(Deck source, @MappingTarget Deck target);
}
