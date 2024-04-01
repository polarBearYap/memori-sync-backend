package com.memori.memori_service.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.memori.memori_data.models.PersistStatus;
import com.memori.memori_data.repositories.sync.SyncEntityAutoRepository;
import com.memori.memori_domain.SyncEntity;
import com.memori.memori_service.SyncRegistry;
import com.memori.memori_service.dtos.PaginatedDto;
import com.memori.memori_service.dtos.PullSyncResponseDto;
import com.memori.memori_service.dtos.SyncEntityDto;
import com.memori.memori_service.dtos.SyncEntityDto.Action;
import com.memori.memori_service.dtos.UserDto;
import com.memori.memori_service.mappers.DateTimeMapper;
import com.memori.memori_service.mappers.SyncEntityMapper;

@Service
public class GeneralSyncService {

    @Autowired
    private SyncRegistry syncRegistry;

    @Autowired
    private UserService userService;

    @Autowired
    private DateTimeMapper dateTimeMapper;

    @Autowired
    SyncEntityAutoRepository syncEntityAutoRepository;

    private Instant validateInput(String lastSyncDateTimeStr, String userId) {
        Instant lastSyncDateTime = dateTimeMapper.asInstant(lastSyncDateTimeStr);

        Optional<UserDto> user = userService.getUserById(userId);
        if (!user.isPresent())
            throw new IllegalArgumentException("The specified user does not exist");

        return lastSyncDateTime;
    }

    @SuppressWarnings("unchecked")
    private SyncEntityService<SyncEntity> getEntityServiceByEntityClassName(String entityClassName) {
        try {
            Class<? extends SyncEntity> entity = (Class<? extends SyncEntity>) Class
                    .forName("com.memori.memori_domain." + entityClassName);
            SyncEntityService<SyncEntity> service = (SyncEntityService<SyncEntity>) syncRegistry.getSafeService(entity);
            return service;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private SyncEntityMapper<SyncEntityDto, SyncEntity> getEntityMapperByEntityClassName(String entityClassName) {
        try {
            Class<? extends SyncEntity> entity = (Class<? extends SyncEntity>) Class
                    .forName("com.memori.memori_domain." + entityClassName);
            SyncEntityMapper<SyncEntityDto, SyncEntity> service = (SyncEntityMapper<SyncEntityDto, SyncEntity>) syncRegistry
                    .getSafeEntityMapper(entity);
            return service;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Transactional(readOnly = true)
    Page<SyncEntity> getCreatedEntities(Instant lastSyncDate, String userId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return syncEntityAutoRepository.findEntitiesByCreatedAtAfterAndUserId(lastSyncDate, userId, pageable);
    }

    @Transactional(readOnly = true)
    Page<SyncEntity> getDeletedEntities(Instant lastSyncDate, String userId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return syncEntityAutoRepository.findEntitiesByDeletedAtAfterAndUserId(lastSyncDate, userId, pageable);
    }

    @Transactional(readOnly = true)
    Page<SyncEntity> getModifiedEntities(Instant lastSyncDate, String userId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return syncEntityAutoRepository.findEntitiesByLastModifiedAfterAndUserId(lastSyncDate, userId, pageable);
    }

    PaginatedDto<SyncEntityDto> pullData(String lastSyncDateTimeStr, String userId, int pageNumber, int pageSize,
            Action action) {
        Instant lastSyncDateTime = validateInput(lastSyncDateTimeStr, userId);

        Page<SyncEntity> result = action == Action.CREATE
                ? getCreatedEntities(lastSyncDateTime, userId, pageNumber, pageSize)
                : action == Action.UPDATE ? getModifiedEntities(lastSyncDateTime, userId, pageNumber, pageSize)
                        : action == Action.DELETE ? getDeletedEntities(lastSyncDateTime, userId, pageNumber, pageSize)
                                : null;
        if (result == null)
            throw new IllegalArgumentException("Action is invalid");
        List<SyncEntityDto> output = new ArrayList<>();

        for (SyncEntity res : result.getContent()) {
            SyncEntityService<SyncEntity> service = getEntityServiceByEntityClassName(res.getEntityType());
            SyncEntityMapper<SyncEntityDto, SyncEntity> mapper = getEntityMapperByEntityClassName(res.getEntityType());
            Optional<SyncEntity> resSubClass = service.getById(res.getId());
            if (resSubClass.isPresent()) {
                SyncEntityDto syncEntityDto = mapper.entityToDto(resSubClass.get());
                syncEntityDto.setAction(action);
                output.add(syncEntityDto);
            }
        }

        PaginatedDto<SyncEntityDto> pDto = new PaginatedDto<>();
        pDto.setTotalPages(result.getTotalPages());
        pDto.setTotalElements(result.getTotalElements());
        pDto.setCurrentPageNumber(result.getNumber());
        pDto.setHasNextPage(result.hasNext());
        pDto.setHasPreviousPage(result.hasPrevious());
        pDto.setContent(output);

        return pDto;
    }

    public PaginatedDto<SyncEntityDto> pullCreation(String lastSyncDateTimeStr, String userId, int pageNumber, int pageSize) {
        return pullData(lastSyncDateTimeStr, userId, pageNumber, pageSize, Action.CREATE);
    }

    public PaginatedDto<SyncEntityDto> pullUpdate(String lastSyncDateTimeStr, String userId, int pageNumber, int pageSize) {
        return pullData(lastSyncDateTimeStr, userId, pageNumber, pageSize, Action.UPDATE);
    }

    public PaginatedDto<SyncEntityDto> pullDeletion(String lastSyncDateTimeStr, String userId, int pageNumber, int pageSize) {
        return pullData(lastSyncDateTimeStr, userId, pageNumber, pageSize, Action.DELETE);
    }

    public PaginatedDto<SyncEntityDto> pullSpecific(List<String> entityIds, String userId) {
        List<UUID> entityUuids = new ArrayList<>();
        for (String entityId : entityIds) {
            entityUuids.add(UUID.fromString(entityId));
        }

        Page<SyncEntity> result = syncEntityAutoRepository.findEntitiesBySpecificIdsAndUserId(entityUuids, userId, PageRequest.of(0, Integer.MAX_VALUE));

        List<SyncEntityDto> output = new ArrayList<>();

        for (SyncEntity res : result.getContent()) {
            SyncEntityService<SyncEntity> service = getEntityServiceByEntityClassName(res.getEntityType());
            SyncEntityMapper<SyncEntityDto, SyncEntity> mapper = getEntityMapperByEntityClassName(res.getEntityType());
            Optional<SyncEntity> resSubClass = service.getById(res.getId());
            if (resSubClass.isPresent()) {
                SyncEntityDto syncEntityDto = mapper.entityToDto(resSubClass.get());
                syncEntityDto.setAction(Action.UPDATE);
                output.add(syncEntityDto);
            }
        }

        PaginatedDto<SyncEntityDto> pDto = new PaginatedDto<>();
        pDto.setTotalPages(result.getTotalPages());
        pDto.setTotalElements(result.getTotalElements());
        pDto.setCurrentPageNumber(result.getNumber());
        pDto.setHasNextPage(result.hasNext());
        pDto.setHasPreviousPage(result.hasPrevious());
        pDto.setContent(output);

        return pDto;
    }

    @SuppressWarnings("unchecked")
    public PullSyncResponseDto normalPush(List<SyncEntityDto> requests) {
        if (requests == null)
            throw new IllegalArgumentException("Requests cannot be null");

        List<SyncEntityDto> successfulRows = new ArrayList<>();
        List<SyncEntityDto> conflictedRows = new ArrayList<>();

        for (SyncEntityDto request : requests) {
            SyncEntityMapper<SyncEntityDto, SyncEntity> mapper = (SyncEntityMapper<SyncEntityDto, SyncEntity>) 
            syncRegistry.getSafeDtoMapper(request.getClass());
            SyncEntity entity = mapper.dtoToEntity(request);
            Action action = request.getAction();
            SyncEntityService<SyncEntity> service = (SyncEntityService<SyncEntity>) 
            syncRegistry.getSafeService(entity.getClass());
            PersistStatus<SyncEntity> persistStatus = null;
            if (action == Action.CREATE) {
                persistStatus = service.create(entity);
            }
            else if (action == Action.UPDATE) {
                persistStatus = service.update(entity);
            }
            else if (action == Action.DELETE) {
                persistStatus = service.delete(entity);
            }
            if (persistStatus == null) {
                continue;
            }
            SyncEntityDto dto = mapper.entityToDto(persistStatus.getEntity());
            dto.setAction(action);
            if (persistStatus.getHasSyncConflict()) {
                conflictedRows.add(dto);
            }
            else {
                successfulRows.add(dto);
            }
        }

        PullSyncResponseDto res = new PullSyncResponseDto();
        res.setSuccessfulItems(successfulRows);
        res.setConflictedItems(conflictedRows);
        return res;
    }

    @SuppressWarnings("unchecked")
    public PullSyncResponseDto overridePush(List<SyncEntityDto> requests) {
        if (requests == null)
            throw new IllegalArgumentException("Requests cannot be null");

        List<SyncEntityDto> successfulRows = new ArrayList<>();

        for (SyncEntityDto request : requests) {
            SyncEntityMapper<SyncEntityDto, SyncEntity> mapper = (SyncEntityMapper<SyncEntityDto, SyncEntity>) 
            syncRegistry.getSafeDtoMapper(request.getClass());
            SyncEntity entity = mapper.dtoToEntity(request);
            Action action = request.getAction();
            SyncEntityService<SyncEntity> service = (SyncEntityService<SyncEntity>) 
            syncRegistry.getSafeService(entity.getClass());
            PersistStatus<SyncEntity> persistStatus = null;
            if (action == Action.CREATE) {
                // service.create(entity);
                persistStatus = service.updateOverride(entity);
            }
            else if (action == Action.UPDATE) {
                persistStatus = service.updateOverride(entity);
            }
            else if (action == Action.DELETE) {
                persistStatus = service.deleteOverride(entity);
            }
            if (persistStatus == null) {
                continue;
            }
            SyncEntityDto dto = mapper.entityToDto(persistStatus.getEntity());
            dto.setAction(action);
            if (!persistStatus.getHasSyncConflict()) {
                successfulRows.add(dto);
            }
        }

        PullSyncResponseDto res = new PullSyncResponseDto();
        res.setSuccessfulItems(successfulRows);
        res.setConflictedItems(new ArrayList<>());
        return res;
    }
}