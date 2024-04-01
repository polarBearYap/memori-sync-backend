package com.memori.memori_data.repositories.sync;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.memori.memori_data.mappers.EntityMapper;
import com.memori.memori_data.models.PersistStatus;
import com.memori.memori_domain.SyncEntity;

@NoRepositoryBean
public interface SyncEntityRepository<T extends SyncEntity> extends JpaRepository<T, UUID> {

    // List<T> getCreatedEntities(Instant lastSyncDate, String userId, int pageNumber, int pageSize);

    // List<T> getDeletedEntities(Instant lastSyncDate, String userId, int pageNumber, int pageSize);

    // List<T> getModifiedEntities(Instant lastSyncDate, String userId, int pageNumber, int pageSize);

    PersistStatus<T> createEntity(T createdEntity);

    PersistStatus<T> updateEntity(T modifiedEntity, EntityMapper<T> mapper);

    PersistStatus<T> deleteEntity(T deletedEntity, EntityMapper<T> mapper);

    PersistStatus<T> updateOverrideEntity(T modifiedEntity, EntityMapper<T> mapper);

    PersistStatus<T> deleteOverrideEntity(T deletedEntity, EntityMapper<T> mapper);

    // PersistStatus<T> updateOrDeleteEntity(T modifiedEntity, EntityMapper<T>
    // mapper, boolean isDeleted, boolean overrideEnabled);

    // boolean hasSyncConflict(SyncEntity entityFromClient, SyncEntity
    // entityFromDatabase);
}