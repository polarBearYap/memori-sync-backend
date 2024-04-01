package com.memori.memori_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.memori.memori_data.mappers.EntityMapper;
import com.memori.memori_data.repositories.sync.SyncEntityRepository;
import com.memori.memori_domain.CardHint;
import com.memori.memori_service.SyncRegistry;

import jakarta.annotation.PostConstruct;

@Service
public class CardHintService extends SyncEntityService<CardHint> {

    @Autowired
    private SyncRegistry syncRegistry;

    @PostConstruct
    public void registerWithRegistry() {
        syncRegistry.registerService(CardHint.class, this);
    }

    public CardHintService(SyncEntityRepository<CardHint> repository, EntityMapper<CardHint> entityMapper) {
        super(repository, entityMapper);
    }
}
