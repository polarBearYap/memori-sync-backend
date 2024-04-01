package com.memori.memori_service.mappers;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import com.memori.memori_domain.DeckSettings;
import com.memori.memori_service.dtos.DeckSettingsDto;

@Mapper(config = SyncEntityMapperConfig.class)
public interface DeckSettingsMapper extends SyncEntityMapper<DeckSettingsDto, DeckSettings> {
    @Override
    @InheritConfiguration(name = "dtoToEntityMapping")
    DeckSettings dtoToEntity(DeckSettingsDto dto);

    @Override
    @InheritInverseConfiguration(name = "dtoToEntity")
    @InheritConfiguration(name = "entityToDtoMapping")
    DeckSettingsDto entityToDto(DeckSettings entity);
}
