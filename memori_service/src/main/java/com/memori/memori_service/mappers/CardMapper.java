package com.memori.memori_service.mappers;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.memori.memori_domain.Card;
import com.memori.memori_service.dtos.CardDto;

@Mapper(config = SyncEntityMapperConfig.class)
public interface CardMapper extends SyncEntityMapper<CardDto, Card> {
    @Override
    @InheritConfiguration(name = "dtoToEntityMapping")
    @Mapping(target = "deck", ignore = true)
    Card dtoToEntity(CardDto dto);

    @Override
    @InheritInverseConfiguration(name = "dtoToEntity")
    @InheritConfiguration(name = "entityToDtoMapping")
    CardDto entityToDto(Card entity);

    /*
     * @Mapper
     * public interface FruitMapper {
     * 
     * @SubclassMapping( source = AppleDto.class, target = Apple.class )
     * 
     * @SubclassMapping( source = BananaDto.class, target = Banana.class )
     * Fruit map( FruitDto source );
     * 
     * }
     */
}