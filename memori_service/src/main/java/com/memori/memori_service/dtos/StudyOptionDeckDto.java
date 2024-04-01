package com.memori.memori_service.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudyOptionDeckDto extends SyncEntityDto {

    private String deckId;

    private String studyOptionId;

    // private DeckDto deck;

    // private StudyOptionDto studyOption;

}