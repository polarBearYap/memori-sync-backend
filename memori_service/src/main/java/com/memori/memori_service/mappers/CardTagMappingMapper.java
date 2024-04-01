package com.memori.memori_service.mappers;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.memori.memori_domain.CardTagMapping;
import com.memori.memori_service.dtos.CardTagMappingDto;

@Mapper(config = SyncEntityMapperConfig.class)
public interface CardTagMappingMapper extends SyncEntityMapper<CardTagMappingDto, CardTagMapping> {
    @Override
    @InheritConfiguration(name = "dtoToEntityMapping")
    @Mapping(target = "card", ignore = true)
    @Mapping(target = "cardTag", ignore = true)
    CardTagMapping dtoToEntity(CardTagMappingDto dto);

    @Override
    @InheritInverseConfiguration(name = "dtoToEntity")
    @InheritConfiguration(name = "entityToDtoMapping")
    CardTagMappingDto entityToDto(CardTagMapping entity);
}