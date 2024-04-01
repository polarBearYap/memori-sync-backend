package com.memori.memori_service.mappers;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.memori.memori_domain.ReviewLog;
import com.memori.memori_service.dtos.ReviewLogDto;

@Mapper(config = SyncEntityMapperConfig.class)
public interface ReviewLogMapper extends SyncEntityMapper<ReviewLogDto, ReviewLog> {
    @Override
    @InheritConfiguration(name = "dtoToEntityMapping")
    @Mapping(target = "card", ignore = true)
    ReviewLog dtoToEntity(ReviewLogDto dto);

    @Override
    @InheritInverseConfiguration(name = "dtoToEntity")
    @InheritConfiguration(name = "entityToDtoMapping")
    ReviewLogDto entityToDto(ReviewLog entity);
}