package com.memori.memori_service.mappers;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import com.memori.memori_domain.CardTag;
import com.memori.memori_service.dtos.CardTagDto;

@Mapper(config = SyncEntityMapperConfig.class)
public interface CardTagMapper extends SyncEntityMapper<CardTagDto, CardTag> {
    @Override
    @InheritConfiguration(name = "dtoToEntityMapping")
    CardTag dtoToEntity(CardTagDto dto);

    @Override
    @InheritInverseConfiguration(name = "dtoToEntity")
    @InheritConfiguration(name = "entityToDtoMapping")
    CardTagDto entityToDto(CardTag entity);
}