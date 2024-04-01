package com.memori.memori_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.memori.memori_data.mappers.EntityMapper;
import com.memori.memori_data.repositories.sync.SyncEntityRepository;
import com.memori.memori_domain.DeckImage;
import com.memori.memori_service.SyncRegistry;

import jakarta.annotation.PostConstruct;

@Service
public class DeckImageService extends SyncEntityService<DeckImage> {

    @Autowired
    private SyncRegistry syncRegistry;

    @PostConstruct
    public void registerWithRegistry() {
        syncRegistry.registerService(DeckImage.class, this);
    }

    public DeckImageService(SyncEntityRepository<DeckImage> repository, EntityMapper<DeckImage> entityMapper) {
        super(repository, entityMapper);
    }
}
