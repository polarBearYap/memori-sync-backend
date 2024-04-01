package com.memori.memori_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.memori.memori_data.mappers.EntityMapper;
import com.memori.memori_data.repositories.sync.SyncEntityRepository;
import com.memori.memori_domain.Card;
import com.memori.memori_service.SyncRegistry;

import jakarta.annotation.PostConstruct;

@Service
public class CardService extends SyncEntityService<Card> {

    @Autowired
    private SyncRegistry syncRegistry;

    @PostConstruct
    public void registerWithRegistry() {
        syncRegistry.registerService(Card.class, this);
    }

    public CardService(SyncEntityRepository<Card> repository, EntityMapper<Card> entityMapper) {
        super(repository, entityMapper);
    }

    public boolean isCardStateValid(int value) {
        return Card.State.isValidCode(value);
    }
}