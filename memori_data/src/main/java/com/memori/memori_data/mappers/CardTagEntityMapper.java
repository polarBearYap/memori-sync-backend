package com.memori.memori_data.mappers;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.memori.memori_domain.CardTag;

@Mapper(config = EntityMapperConfig.class)
public interface CardTagEntityMapper extends EntityMapper<CardTag> {
    @Override
    void mapEntity(CardTag source, @MappingTarget CardTag target);

    @Override
    @InheritConfiguration(name = "updateEntity")
    void updateEntity(CardTag source, @MappingTarget CardTag target);

    @Override
    @InheritConfiguration(name = "deleteEntity")
    void deleteEntity(CardTag source, @MappingTarget CardTag target);
}
