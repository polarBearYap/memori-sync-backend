package com.memori.memori_service.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeckTagMappingDto extends SyncEntityDto {

    private String deckId;

    private String deckTagId;

    // private DeckDto deck;

    // private DeckTagDto deckTag;
}
