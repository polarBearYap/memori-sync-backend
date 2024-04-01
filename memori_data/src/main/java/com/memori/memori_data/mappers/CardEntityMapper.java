package com.memori.memori_data.mappers;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.memori.memori_domain.Card;

@Mapper(config = EntityMapperConfig.class)
public interface CardEntityMapper extends EntityMapper<Card> {
    @Override
    void mapEntity(Card source, @MappingTarget Card target);

    @Override
    @InheritConfiguration(name = "updateEntity")
    void updateEntity(Card source, @MappingTarget Card target);

    @Override
    @InheritConfiguration(name = "deleteEntity")
    void deleteEntity(Card source, @MappingTarget Card target);
}
