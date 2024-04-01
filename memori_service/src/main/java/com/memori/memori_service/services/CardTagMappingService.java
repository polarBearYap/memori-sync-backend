package com.memori.memori_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.memori.memori_data.mappers.EntityMapper;
import com.memori.memori_data.repositories.sync.SyncEntityRepository;
import com.memori.memori_domain.CardTagMapping;
import com.memori.memori_service.SyncRegistry;

import jakarta.annotation.PostConstruct;

@Service
public class CardTagMappingService extends SyncEntityService<CardTagMapping> {

    @Autowired
    private SyncRegistry syncRegistry;

    @PostConstruct
    public void registerWithRegistry() {
        syncRegistry.registerService(CardTagMapping.class, this);
    }

    public CardTagMappingService(SyncEntityRepository<CardTagMapping> repository, EntityMapper<CardTagMapping> entityMapper) {
        super(repository, entityMapper);
    }
}
