package com.memori.memori_data.mappers;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.memori.memori_domain.ReviewLog;

@Mapper(config = EntityMapperConfig.class)
public interface ReviewLogEntityMapper extends EntityMapper<ReviewLog> {
    @Override
    void mapEntity(ReviewLog source, @MappingTarget ReviewLog target);

    @Override
    @InheritConfiguration(name = "updateEntity")
    void updateEntity(ReviewLog source, @MappingTarget ReviewLog target);

    @Override
    @InheritConfiguration(name = "deleteEntity")
    void deleteEntity(ReviewLog source, @MappingTarget ReviewLog target);
}
