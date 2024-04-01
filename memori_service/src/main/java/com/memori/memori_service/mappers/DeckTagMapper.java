package com.memori.memori_service.mappers;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import com.memori.memori_domain.DeckTag;
import com.memori.memori_service.dtos.DeckTagDto;

@Mapper(config = SyncEntityMapperConfig.class)
public interface DeckTagMapper extends SyncEntityMapper<DeckTagDto, DeckTag> {
    @Override
    @InheritConfiguration(name = "dtoToEntityMapping")
    DeckTag dtoToEntity(DeckTagDto dto);

    @Override
    @InheritInverseConfiguration(name = "dtoToEntity")
    @InheritConfiguration(name = "entityToDtoMapping")
    DeckTagDto entityToDto(DeckTag entity);
}