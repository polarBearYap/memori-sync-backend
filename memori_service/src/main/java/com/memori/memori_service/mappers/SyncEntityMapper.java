package com.memori.memori_service.mappers;

import com.memori.memori_domain.SyncEntity;
import com.memori.memori_service.dtos.SyncEntityDto;

public interface SyncEntityMapper<D extends SyncEntityDto, E extends SyncEntity> {
    D entityToDto(E entity);

    E dtoToEntity(D dto);
}