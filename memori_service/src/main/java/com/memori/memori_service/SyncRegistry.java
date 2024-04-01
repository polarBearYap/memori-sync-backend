package com.memori.memori_service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.memori.memori_domain.SyncEntity;
import com.memori.memori_service.dtos.SyncEntityDto;
import com.memori.memori_service.mappers.SyncEntityMapper;
import com.memori.memori_service.services.SyncEntityService;

@Component
public class SyncRegistry {
    private final Map<Class<? extends SyncEntityDto>, SyncEntityMapper<? extends SyncEntityDto, ? extends SyncEntity>> dtoMapperRegistry = new HashMap<>();
    private final Map<Class<? extends SyncEntity>, SyncEntityMapper<? extends SyncEntityDto, ? extends SyncEntity>> entityMapperRegistry = new HashMap<>();
    private final Map<Class<? extends SyncEntity>, SyncEntityService<? extends SyncEntity>> serviceRegistry = new HashMap<>();

    public <D extends SyncEntityDto, E extends SyncEntity> void registerMapper(Class<D> dtoClass, Class<E> entityClass, SyncEntityMapper<D, E> mapper) {
        dtoMapperRegistry.put(dtoClass, mapper);
        entityMapperRegistry.put(entityClass, mapper);
    }

    public <E extends SyncEntity> void registerService(Class<E> entityClass, SyncEntityService<E> service) {
        serviceRegistry.put(entityClass, service);
    }

    @SuppressWarnings("unchecked")
    public <D extends SyncEntityDto, E extends SyncEntity> SyncEntityMapper<D, E> getSafeDtoMapper(Class<D> dtoClass) {
        SyncEntityMapper<D, E> mapper = (SyncEntityMapper<D, E>) dtoMapperRegistry.get(dtoClass);
        if (mapper == null) {
            throw new IllegalArgumentException("No mapper registered for " + dtoClass.getName());
        }
        return mapper;
    }

    @SuppressWarnings("unchecked")
    public <D extends SyncEntityDto, E extends SyncEntity> SyncEntityMapper<D, E> getSafeEntityMapper(Class<E> entityClass) {
        SyncEntityMapper<D, E> mapper = (SyncEntityMapper<D, E>) entityMapperRegistry.get(entityClass);
        if (mapper == null) {
            throw new IllegalArgumentException("No mapper registered for " + entityClass.getName());
        }
        return mapper;
    }

    @SuppressWarnings("unchecked")
    public <E extends SyncEntity> SyncEntityService<E> getSafeService(Class<E> entityClass) {
        SyncEntityService<E> service = (SyncEntityService<E>) serviceRegistry.get(entityClass);
        if (service == null) {
            throw new IllegalArgumentException("No service registered for " + entityClass.getName());
        }
        return service;
    }
}
