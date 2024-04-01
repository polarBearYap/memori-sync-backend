package com.memori.memori_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.memori.memori_data.mappers.EntityMapper;
import com.memori.memori_data.repositories.sync.SyncEntityRepository;
import com.memori.memori_domain.StudyOptionTag;
import com.memori.memori_service.SyncRegistry;

import jakarta.annotation.PostConstruct;

@Service
public class StudyOptionTagService extends SyncEntityService<StudyOptionTag> {

    @Autowired
    private SyncRegistry syncRegistry;

    @PostConstruct
    public void registerWithRegistry() {
        syncRegistry.registerService(StudyOptionTag.class, this);
    }

    public StudyOptionTagService(SyncEntityRepository<StudyOptionTag> repository, EntityMapper<StudyOptionTag> entityMapper) {
        super(repository, entityMapper);
    }
}
