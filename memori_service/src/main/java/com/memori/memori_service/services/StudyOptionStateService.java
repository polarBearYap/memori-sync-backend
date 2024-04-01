package com.memori.memori_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.memori.memori_data.mappers.EntityMapper;
import com.memori.memori_data.repositories.sync.SyncEntityRepository;
import com.memori.memori_domain.StudyOptionState;
import com.memori.memori_service.SyncRegistry;

import jakarta.annotation.PostConstruct;

@Service
public class StudyOptionStateService extends SyncEntityService<StudyOptionState> {

    @Autowired
    private SyncRegistry syncRegistry;

    @PostConstruct
    public void registerWithRegistry() {
        syncRegistry.registerService(StudyOptionState.class, this);
    }

    public StudyOptionStateService(SyncEntityRepository<StudyOptionState> repository, EntityMapper<StudyOptionState> entityMapper) {
        super(repository, entityMapper);
    }
}
