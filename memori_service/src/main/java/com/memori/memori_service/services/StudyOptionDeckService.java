package com.memori.memori_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.memori.memori_data.mappers.EntityMapper;
import com.memori.memori_data.repositories.sync.SyncEntityRepository;
import com.memori.memori_domain.StudyOptionDeck;
import com.memori.memori_service.SyncRegistry;

import jakarta.annotation.PostConstruct;

@Service
public class StudyOptionDeckService extends SyncEntityService<StudyOptionDeck> {

    @Autowired
    private SyncRegistry syncRegistry;

    @PostConstruct
    public void registerWithRegistry() {
        syncRegistry.registerService(StudyOptionDeck.class, this);
    }

    public StudyOptionDeckService(SyncEntityRepository<StudyOptionDeck> repository, EntityMapper<StudyOptionDeck> entityMapper) {
        super(repository, entityMapper);
    }
}
