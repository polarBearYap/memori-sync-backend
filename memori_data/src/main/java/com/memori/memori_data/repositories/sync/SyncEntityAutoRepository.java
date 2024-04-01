package com.memori.memori_data.repositories.sync;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.memori.memori_domain.SyncEntity;

@Repository
public interface SyncEntityAutoRepository extends JpaRepository<SyncEntity, UUID> {
    @Query("SELECT e FROM SyncEntity e WHERE e.userId = :userId AND e.createdAt > :lastSyncDate ORDER BY e.sortOrder ASC, e.createdAt ASC")
    Page<SyncEntity> findEntitiesByCreatedAtAfterAndUserId(Instant lastSyncDate, String userId, Pageable pageable);

    @Query("SELECT e FROM SyncEntity e WHERE e.userId = :userId AND e.createdAt <= :lastSyncDate AND e.deletedAt IS NOT NULL AND e.deletedAt > :lastSyncDate ORDER BY e.deletedAt ASC")
    Page<SyncEntity> findEntitiesByDeletedAtAfterAndUserId(Instant lastSyncDate, String userId, Pageable pageable);

    @Query("SELECT e FROM SyncEntity e WHERE e.userId = :userId AND e.createdAt <= :lastSyncDate AND e.deletedAt IS NULL AND e.lastModified > :lastSyncDate ORDER BY e.lastModified ASC")
    Page<SyncEntity> findEntitiesByLastModifiedAfterAndUserId(Instant lastSyncDate, String userId, Pageable pageable);

    @Query("SELECT e FROM SyncEntity e WHERE e.userId = :userId AND e.id IN :ids")
    Page<SyncEntity> findEntitiesBySpecificIdsAndUserId(@Param("ids") List<UUID> ids, @Param("userId") String userId, Pageable pageable);
}
