package com.memori.memori_service.mappers;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.memori.memori_domain.DeckImage;
import com.memori.memori_service.dtos.DeckImageDto;

@Mapper(config = SyncEntityMapperConfig.class)
public interface DeckImageMapper extends SyncEntityMapper<DeckImageDto, DeckImage> {
    @Override
    @InheritConfiguration(name = "dtoToEntityMapping")
    @Mapping(target = "deck", ignore = true)
    DeckImage dtoToEntity(DeckImageDto dto);

    @Override
    @InheritInverseConfiguration(name = "dtoToEntity")
    @InheritConfiguration(name = "entityToDtoMapping")
    DeckImageDto entityToDto(DeckImage entity);
}
