package com.memori.memori_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.memori.memori_data.mappers.EntityMapper;
import com.memori.memori_data.repositories.sync.SyncEntityRepository;
import com.memori.memori_domain.CardTag;
import com.memori.memori_service.SyncRegistry;

import jakarta.annotation.PostConstruct;

@Service
public class CardTagService extends SyncEntityService<CardTag> {

    @Autowired
    private SyncRegistry syncRegistry;

    @PostConstruct
    public void registerWithRegistry() {
        syncRegistry.registerService(CardTag.class, this);
    }

    public CardTagService(SyncEntityRepository<CardTag> repository, EntityMapper<CardTag> entityMapper) {
        super(repository, entityMapper);
    }
}
