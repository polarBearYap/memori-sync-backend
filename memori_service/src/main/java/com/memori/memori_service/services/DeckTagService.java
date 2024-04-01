package com.memori.memori_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.memori.memori_data.mappers.EntityMapper;
import com.memori.memori_data.repositories.sync.SyncEntityRepository;
import com.memori.memori_domain.DeckTag;
import com.memori.memori_service.SyncRegistry;

import jakarta.annotation.PostConstruct;

@Service
public class DeckTagService extends SyncEntityService<DeckTag> {

    @Autowired
    private SyncRegistry syncRegistry;

    @PostConstruct
    public void registerWithRegistry() {
        syncRegistry.registerService(DeckTag.class, this);
    }

    public DeckTagService(SyncEntityRepository<DeckTag> repository, EntityMapper<DeckTag> entityMapper) {
        super(repository, entityMapper);
    }
}
