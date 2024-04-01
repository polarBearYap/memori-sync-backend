package com.memori.memori_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.memori.memori_data.mappers.EntityMapper;
import com.memori.memori_data.repositories.sync.SyncEntityRepository;
import com.memori.memori_domain.Deck;
import com.memori.memori_service.SyncRegistry;

import jakarta.annotation.PostConstruct;

@Service
public class DeckService extends SyncEntityService<Deck> {

    @Autowired
    private SyncRegistry syncRegistry;

    @PostConstruct
    public void registerWithRegistry() {
        syncRegistry.registerService(Deck.class, this);
    }

    public DeckService(SyncEntityRepository<Deck> repository, EntityMapper<Deck> entityMapper) {
        super(repository, entityMapper);
    }
}
