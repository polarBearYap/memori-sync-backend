package com.memori.memori_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.memori.memori_data.mappers.EntityMapper;
import com.memori.memori_data.repositories.sync.SyncEntityRepository;
import com.memori.memori_domain.DeckTagMapping;
import com.memori.memori_service.SyncRegistry;

import jakarta.annotation.PostConstruct;

@Service
public class DeckTagMappingService extends SyncEntityService<DeckTagMapping> {

    @Autowired
    private SyncRegistry syncRegistry;

    @PostConstruct
    public void registerWithRegistry() {
        syncRegistry.registerService(DeckTagMapping.class, this);
    }

    public DeckTagMappingService(SyncEntityRepository<DeckTagMapping> repository, EntityMapper<DeckTagMapping> entityMapper) {
        super(repository, entityMapper);
    }
}
