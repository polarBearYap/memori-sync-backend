package com.memori.memori_service;

import org.springframework.context.annotation.Configuration;

import com.memori.memori_domain.Card;
import com.memori.memori_domain.CardHint;
import com.memori.memori_domain.CardTag;
import com.memori.memori_domain.CardTagMapping;
import com.memori.memori_domain.Deck;
import com.memori.memori_domain.DeckImage;
import com.memori.memori_domain.DeckReviews;
import com.memori.memori_domain.DeckSettings;
import com.memori.memori_domain.DeckTag;
import com.memori.memori_domain.DeckTagMapping;
import com.memori.memori_domain.ReviewLog;
import com.memori.memori_domain.StudyOption;
import com.memori.memori_domain.StudyOptionDeck;
import com.memori.memori_domain.StudyOptionState;
import com.memori.memori_domain.StudyOptionTag;
import com.memori.memori_service.dtos.CardDto;
import com.memori.memori_service.dtos.CardHintDto;
import com.memori.memori_service.dtos.CardTagDto;
import com.memori.memori_service.dtos.CardTagMappingDto;
import com.memori.memori_service.dtos.DeckDto;
import com.memori.memori_service.dtos.DeckImageDto;
import com.memori.memori_service.dtos.DeckReviewsDto;
import com.memori.memori_service.dtos.DeckSettingsDto;
import com.memori.memori_service.dtos.DeckTagDto;
import com.memori.memori_service.dtos.DeckTagMappingDto;
import com.memori.memori_service.dtos.ReviewLogDto;
import com.memori.memori_service.dtos.StudyOptionDeckDto;
import com.memori.memori_service.dtos.StudyOptionDto;
import com.memori.memori_service.dtos.StudyOptionStateDto;
import com.memori.memori_service.dtos.StudyOptionTagDto;
import com.memori.memori_service.mappers.CardHintMapper;
import com.memori.memori_service.mappers.CardMapper;
import com.memori.memori_service.mappers.CardTagMapper;
import com.memori.memori_service.mappers.CardTagMappingMapper;
import com.memori.memori_service.mappers.DeckImageMapper;
import com.memori.memori_service.mappers.DeckMapper;
import com.memori.memori_service.mappers.DeckReviewsMapper;
import com.memori.memori_service.mappers.DeckSettingsMapper;
import com.memori.memori_service.mappers.DeckTagMapper;
import com.memori.memori_service.mappers.DeckTagMappingMapper;
import com.memori.memori_service.mappers.ReviewLogMapper;
import com.memori.memori_service.mappers.StudyOptionDeckMapper;
import com.memori.memori_service.mappers.StudyOptionMapper;
import com.memori.memori_service.mappers.StudyOptionStateMapper;
import com.memori.memori_service.mappers.StudyOptionTagMapper;

import jakarta.annotation.PostConstruct;

@Configuration
public class MapperRegistryConfig {

    private final CardMapper cardMapper;
    private final CardHintMapper cardHintMapper;
    private final CardTagMapper cardTagMapper;
    private final CardTagMappingMapper cardTagMappingMapper;
    private final DeckMapper deckMapper;
    private final DeckImageMapper deckImageMapper;
    private final DeckReviewsMapper deckReviewsMapper;
    private final DeckSettingsMapper deckSettingsMapper;
    private final DeckTagMapper deckTagMapper;
    private final DeckTagMappingMapper deckTagMappingMapper;
    private final ReviewLogMapper reviewLogMapper;
    private final StudyOptionMapper studyOptionMapper;
    private final StudyOptionDeckMapper studyOptionDeckMapper;
    private final StudyOptionStateMapper studyOptionStateMapper;
    private final StudyOptionTagMapper studyOptionTagMapper;
    
    private final SyncRegistry syncRegistry;

    public MapperRegistryConfig(
        CardMapper cardMapper,
        CardHintMapper cardHintMapper,
        CardTagMapper cardTagMapper,
        CardTagMappingMapper cardTagMappingMapper,
        DeckMapper deckMapper,
        DeckImageMapper deckImageMapper,
        DeckReviewsMapper deckReviewsMapper,
        DeckSettingsMapper deckSettingsMapper, 
        DeckTagMapper deckTagMapper,
        DeckTagMappingMapper deckTagMappingMapper,
        ReviewLogMapper reviewLogMapper, 
        StudyOptionMapper studyOptionMapper, 
        SyncRegistry syncRegistry,
        StudyOptionDeckMapper studyOptionDeckMapper,
        StudyOptionStateMapper studyOptionStateMapper,
        StudyOptionTagMapper studyOptionTagMapper
    ) {
        this.cardMapper = cardMapper;
        this.cardHintMapper = cardHintMapper;
        this.cardTagMapper = cardTagMapper;
        this.cardTagMappingMapper = cardTagMappingMapper;
        this.deckMapper = deckMapper;
        this.deckImageMapper = deckImageMapper;
        this.deckReviewsMapper = deckReviewsMapper;
        this.deckSettingsMapper = deckSettingsMapper;
        this.deckTagMapper = deckTagMapper;
        this.deckTagMappingMapper = deckTagMappingMapper;
        this.studyOptionMapper = studyOptionMapper;
        this.reviewLogMapper = reviewLogMapper;
        this.syncRegistry = syncRegistry;
        this.studyOptionDeckMapper = studyOptionDeckMapper;
        this.studyOptionStateMapper = studyOptionStateMapper;
        this.studyOptionTagMapper = studyOptionTagMapper;
    }

    @PostConstruct
    public void registerMappers() {
        syncRegistry.registerMapper(CardDto.class, Card.class, cardMapper);
        syncRegistry.registerMapper(CardHintDto.class, CardHint.class, cardHintMapper);
        syncRegistry.registerMapper(CardTagDto.class, CardTag.class, cardTagMapper);
        syncRegistry.registerMapper(CardTagMappingDto.class, CardTagMapping.class, cardTagMappingMapper);
        syncRegistry.registerMapper(DeckDto.class, Deck.class, deckMapper);
        syncRegistry.registerMapper(DeckImageDto.class, DeckImage.class, deckImageMapper);
        syncRegistry.registerMapper(DeckReviewsDto.class, DeckReviews.class, deckReviewsMapper);
        syncRegistry.registerMapper(DeckSettingsDto.class, DeckSettings.class, deckSettingsMapper);
        syncRegistry.registerMapper(DeckTagDto.class, DeckTag.class, deckTagMapper);
        syncRegistry.registerMapper(DeckTagMappingDto.class, DeckTagMapping.class, deckTagMappingMapper);
        syncRegistry.registerMapper(StudyOptionDto.class, StudyOption.class, studyOptionMapper);
        syncRegistry.registerMapper(ReviewLogDto.class, ReviewLog.class, reviewLogMapper);
        syncRegistry.registerMapper(StudyOptionDeckDto.class, StudyOptionDeck.class, studyOptionDeckMapper);
        syncRegistry.registerMapper(StudyOptionStateDto.class, StudyOptionState.class, studyOptionStateMapper);
        syncRegistry.registerMapper(StudyOptionTagDto.class, StudyOptionTag.class, studyOptionTagMapper);
    }
}
