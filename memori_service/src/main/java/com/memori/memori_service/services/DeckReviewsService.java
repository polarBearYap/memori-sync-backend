package com.memori.memori_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.memori.memori_data.mappers.EntityMapper;
import com.memori.memori_data.repositories.sync.SyncEntityRepository;
import com.memori.memori_domain.DeckReviews;
import com.memori.memori_service.SyncRegistry;

import jakarta.annotation.PostConstruct;

@Service
public class DeckReviewsService extends SyncEntityService<DeckReviews> {

    @Autowired
    private SyncRegistry syncRegistry;

    @PostConstruct
    public void registerWithRegistry() {
        syncRegistry.registerService(DeckReviews.class, this);
    }

    public DeckReviewsService(SyncEntityRepository<DeckReviews> repository, EntityMapper<DeckReviews> entityMapper) {
        super(repository, entityMapper);
    }
}
