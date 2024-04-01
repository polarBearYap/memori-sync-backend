package com.memori.memori_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.memori.memori_data.mappers.EntityMapper;
import com.memori.memori_data.repositories.sync.SyncEntityRepository;
import com.memori.memori_domain.ReviewLog;
import com.memori.memori_service.SyncRegistry;

import jakarta.annotation.PostConstruct;

@Service
public class ReviewLogService extends SyncEntityService<ReviewLog> {

    @Autowired
    private SyncRegistry syncRegistry;

    @PostConstruct
    public void registerWithRegistry() {
        syncRegistry.registerService(ReviewLog.class, this);
    }

    public ReviewLogService(SyncEntityRepository<ReviewLog> repository,  EntityMapper<ReviewLog> entityMapper) {
        super(repository, entityMapper);
    }

    public boolean isCardRatingValid(int value) {
        return ReviewLog.Rating.isValidCode(value);
    }
}