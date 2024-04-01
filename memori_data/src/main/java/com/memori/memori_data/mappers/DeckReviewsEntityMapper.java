package com.memori.memori_data.mappers;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.memori.memori_domain.DeckReviews;

@Mapper(config = EntityMapperConfig.class)
public interface DeckReviewsEntityMapper extends EntityMapper<DeckReviews> {
    @Override
    void mapEntity(DeckReviews source, @MappingTarget DeckReviews target);

    @Override
    @InheritConfiguration(name = "updateEntity")
    void updateEntity(DeckReviews source, @MappingTarget DeckReviews target);

    @Override
    @InheritConfiguration(name = "deleteEntity")
    void deleteEntity(DeckReviews source, @MappingTarget DeckReviews target);
}
