package com.memori.memori_data.mappers;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.memori.memori_domain.CardTagMapping;

@Mapper(config = EntityMapperConfig.class)
public interface CardTagMappingEntityMapper extends EntityMapper<CardTagMapping> {
    @Override
    void mapEntity(CardTagMapping source, @MappingTarget CardTagMapping target);

    @Override
    @InheritConfiguration(name = "updateEntity")
    void updateEntity(CardTagMapping source, @MappingTarget CardTagMapping target);

    @Override
    @InheritConfiguration(name = "deleteEntity")
    void deleteEntity(CardTagMapping source, @MappingTarget CardTagMapping target);
}
