package com.memori.memori_data.mappers;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.memori.memori_domain.CardHint;

@Mapper(config = EntityMapperConfig.class)
public interface CardHintEntityMapper extends EntityMapper<CardHint> {
    @Override
    void mapEntity(CardHint source, @MappingTarget CardHint target);

    @Override
    @InheritConfiguration(name = "updateEntity")
    void updateEntity(CardHint source, @MappingTarget CardHint target);

    @Override
    @InheritConfiguration(name = "deleteEntity")
    void deleteEntity(CardHint source, @MappingTarget CardHint target);
}
