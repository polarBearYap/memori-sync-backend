package com.memori.memori_service.mappers;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.memori.memori_domain.DeckReviews;
import com.memori.memori_service.dtos.DeckReviewsDto;

@Mapper(config = SyncEntityMapperConfig.class)
public interface DeckReviewsMapper extends SyncEntityMapper<DeckReviewsDto, DeckReviews> {
    @Override
    @InheritConfiguration(name = "dtoToEntityMapping")
    @Mapping(target = "deck", ignore = true)
    DeckReviews dtoToEntity(DeckReviewsDto dto);

    @Override
    @InheritInverseConfiguration(name = "dtoToEntity")
    @InheritConfiguration(name = "entityToDtoMapping")
    DeckReviewsDto entityToDto(DeckReviews entity);
}
