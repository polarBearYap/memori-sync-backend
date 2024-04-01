package com.memori.memori_service.mappers;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.memori.memori_domain.DeckTagMapping;
import com.memori.memori_service.dtos.DeckTagMappingDto;

@Mapper(config = SyncEntityMapperConfig.class)
public interface DeckTagMappingMapper extends SyncEntityMapper<DeckTagMappingDto, DeckTagMapping> {
    @Override
    @InheritConfiguration(name = "dtoToEntityMapping")
    @Mapping(target = "deck", ignore = true)
    @Mapping(target = "deckTag", ignore = true)
    DeckTagMapping dtoToEntity(DeckTagMappingDto dto);

    @Override
    @InheritInverseConfiguration(name = "dtoToEntity")
    @InheritConfiguration(name = "entityToDtoMapping")
    DeckTagMappingDto entityToDto(DeckTagMapping entity);
}