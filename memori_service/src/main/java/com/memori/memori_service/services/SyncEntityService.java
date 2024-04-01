package com.memori.memori_service.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import com.memori.memori_data.mappers.EntityMapper;
import com.memori.memori_data.models.PersistStatus;
import com.memori.memori_data.repositories.sync.SyncEntityRepository;
import com.memori.memori_domain.SyncEntity;

@NoRepositoryBean
public abstract class SyncEntityService<T extends SyncEntity> {

    private final SyncEntityRepository<T> repository;
    private final EntityMapper<T> entityMapper;

    public SyncEntityService(SyncEntityRepository<T> repository, EntityMapper<T> entityMapper) {
        this.repository = repository;
        this.entityMapper = entityMapper;
    }

    @Transactional(readOnly = true)
    public Optional<T> getById(String id) {
        if (id == null)
            throw new IllegalArgumentException("id must not be null");
        UUID uuid = UUID.fromString(id);
        if (uuid == null)
            throw new IllegalArgumentException("id is invalid");
        return repository.findById(uuid);
    }

    @Transactional(readOnly = true)
    public Optional<T> getById(UUID id) {
        if (id == null)
            throw new IllegalArgumentException("id must not be null");
        return repository.findById(id);
    }

    @Transactional
    public PersistStatus<T> create(T entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must not be null");
        return repository.createEntity(entity);
    }

    @Transactional
    public PersistStatus<T> update(T entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must not be null");
        return repository.updateEntity(entity, entityMapper);
    }

    @Transactional
    public PersistStatus<T> delete(T entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must not be null");
        return repository.deleteEntity(entity, entityMapper);
    }

    @Transactional
    public PersistStatus<T> updateOverride(T entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must not be null");
        return repository.updateOverrideEntity(entity, entityMapper);
    }

    @Transactional
    public PersistStatus<T> deleteOverride(T entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must not be null");
        return repository.deleteOverrideEntity(entity, entityMapper);
    }
}
