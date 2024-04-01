package com.memori.memori_service.mappers;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.memori.memori_domain.CardHint;
import com.memori.memori_service.dtos.CardHintDto;

@Mapper(config = SyncEntityMapperConfig.class)
public interface CardHintMapper extends SyncEntityMapper<CardHintDto, CardHint> {
    @Override
    @InheritConfiguration(name = "dtoToEntityMapping")
    @Mapping(target = "card", ignore = true)
    CardHint dtoToEntity(CardHintDto dto);

    @Override
    @InheritInverseConfiguration(name = "dtoToEntity")
    @InheritConfiguration(name = "entityToDtoMapping")
    CardHintDto entityToDto(CardHint entity);
}