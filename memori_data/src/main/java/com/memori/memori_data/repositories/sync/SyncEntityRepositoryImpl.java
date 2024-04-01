package com.memori.memori_data.repositories.sync;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.memori.memori_data.mappers.EntityMapper;
import com.memori.memori_data.models.PersistStatus;
import com.memori.memori_domain.SyncEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;

public class SyncEntityRepositoryImpl<T extends SyncEntity> extends SimpleJpaRepository<T, UUID>
        implements SyncEntityRepository<T> {

    public SyncEntityRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    @Transactional
    public PersistStatus<T> createEntity(T createdEntity) {
        // The columns below should not be null, it should be propagated from mobile
        // backend
        if (createdEntity == null)
            throw new IllegalArgumentException("entity must not be null");

        UUID id = createdEntity.getId();
        if (id == null)
            throw new IllegalArgumentException("id must not be null");

        if (createdEntity.getCreatedAt() == null)
            throw new IllegalArgumentException("created date must not be null");

        if (createdEntity.getLastModified() == null)
            createdEntity.setLastModified(createdEntity.getCreatedAt());

        if (createdEntity.getModifiedByDeviceId() == null)
            throw new IllegalArgumentException("device id must not be null");

        Optional<T> entityFromDatabase = findById(id);

        if (entityFromDatabase.isPresent())
            // throw new EntityExistsException("Entity with id" + id + "already exist");
            return PersistStatus.of(false, true, entityFromDatabase.get());

        createdEntity.setVersion(0l);
        createdEntity.setDeletedAt(null);
        createdEntity.setSyncedAt(Instant.now());
        createdEntity.setSortOrder(createdEntity.getSortOrderConstant());

        createdEntity = save(createdEntity);
        createdEntity.setEntityType(createdEntity.getEntityType());

        return PersistStatus.of(true, false, createdEntity);
    }

    @Transactional
    public PersistStatus<T> updateEntity(T modifiedEntity, EntityMapper<T> mapper) {
        return updateOrDeleteEntity(modifiedEntity, mapper, false, false);
    }

    @Transactional
    public PersistStatus<T> deleteEntity(T deletedEntity, EntityMapper<T> mapper) {
        return updateOrDeleteEntity(deletedEntity, mapper, true, false);
    }

    @Transactional
    public PersistStatus<T> updateOverrideEntity(T modifiedEntity, EntityMapper<T> mapper) {
        return updateOrDeleteEntity(modifiedEntity, mapper, false, true);
    }

    @Transactional
    public PersistStatus<T> deleteOverrideEntity(T deletedEntity, EntityMapper<T> mapper) {
        return updateOrDeleteEntity(deletedEntity, mapper, true, true);
    }

    @Transactional
    PersistStatus<T> updateOrDeleteEntity(T modifiedEntity, EntityMapper<T> mapper, boolean isDeleted,
            boolean overrideEnabled) {
        // The columns below should not be null, it should be originated from mobile
        // backend
        UUID id = modifiedEntity.getId();
        if (id == null)
            throw new IllegalArgumentException("id must not be null");

        if (modifiedEntity.getLastModified() == null)
            throw new IllegalArgumentException("last modified must not be null");

        if (modifiedEntity.getModifiedByDeviceId() == null)
            throw new IllegalArgumentException("device id must not be null");

        if (isDeleted && modifiedEntity.getDeletedAt() == null)
            throw new IllegalArgumentException("deleted at must not be null");

        T entityFromDatabase = findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Entity with id " + id + " not found"));

        if (!overrideEnabled && hasSyncConflict(modifiedEntity, entityFromDatabase))
            return PersistStatus.of(false, true, entityFromDatabase);

        // Increment the version number
        entityFromDatabase.setVersion(entityFromDatabase.getVersion() + 1);

        // Set cloud sync time
        entityFromDatabase.setSyncedAt(Instant.now());

        // Map properties from modifiedEntity to entityFromDatabase
        if (isDeleted)
            mapper.deleteEntity(modifiedEntity, entityFromDatabase);
        else
            mapper.updateEntity(modifiedEntity, entityFromDatabase);

        // Persist the deleted entity (soft delete)
        entityFromDatabase = save(entityFromDatabase);
        entityFromDatabase.setEntityType(entityFromDatabase.getEntityType());

        return PersistStatus.of(true, false, entityFromDatabase);
    }

    @Transactional
    boolean hasSyncConflict(SyncEntity entityFromClient, SyncEntity entityFromDatabase) {
        // Other device has updated before the current device
        if (!entityFromClient.getVersion().equals(entityFromDatabase.getVersion()))
            return true;
        return false;
    }

}
