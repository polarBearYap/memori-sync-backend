package com.memori.memori_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.memori.memori_data.mappers.EntityMapper;
import com.memori.memori_data.repositories.sync.SyncEntityRepository;
import com.memori.memori_domain.StudyOption;
import com.memori.memori_service.SyncRegistry;

import jakarta.annotation.PostConstruct;

@Service
public class StudyOptionService extends SyncEntityService<StudyOption> {

    @Autowired
    private SyncRegistry syncRegistry;

    @PostConstruct
    public void registerWithRegistry() {
        syncRegistry.registerService(StudyOption.class, this);
    }

    public StudyOptionService(SyncEntityRepository<StudyOption> repository,  EntityMapper<StudyOption> entityMapper) {
        super(repository, entityMapper);
    }
}