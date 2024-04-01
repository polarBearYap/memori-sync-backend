package com.memori.memori_service.mappers;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.memori.memori_domain.Deck;
import com.memori.memori_service.dtos.DeckDto;

@Mapper(config = SyncEntityMapperConfig.class)
public interface DeckMapper extends SyncEntityMapper<DeckDto, Deck> {
    @Override
    @InheritConfiguration(name = "dtoToEntityMapping")
    @Mapping(target = "lastReviewTime", defaultExpression = "java(null)")
    @Mapping(target = "deckSettings", ignore = true)
    Deck dtoToEntity(DeckDto dto);

    @Override
    @InheritInverseConfiguration(name = "dtoToEntity")
    @InheritConfiguration(name = "entityToDtoMapping")
    @Mapping(target = "lastReviewTime", defaultExpression = "java(null)")
    DeckDto entityToDto(Deck entity);
}