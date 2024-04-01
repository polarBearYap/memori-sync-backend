package com.memori.memori_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.memori.memori_data.mappers.EntityMapper;
import com.memori.memori_data.repositories.sync.SyncEntityRepository;
import com.memori.memori_domain.DeckSettings;
import com.memori.memori_service.SyncRegistry;

import jakarta.annotation.PostConstruct;

@Service
public class DeckSettingsService extends SyncEntityService<DeckSettings> {

    @Autowired
    private SyncRegistry syncRegistry;

    @PostConstruct
    public void registerWithRegistry() {
        syncRegistry.registerService(DeckSettings.class, this);
    }

    public DeckSettingsService(SyncEntityRepository<DeckSettings> repository, EntityMapper<DeckSettings> entityMapper) {
        super(repository, entityMapper);
    }
}