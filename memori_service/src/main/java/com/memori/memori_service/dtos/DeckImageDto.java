package com.memori.memori_service.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeckImageDto extends SyncEntityDto {

    private String deckId;

    // private DeckDto deck;

    private byte[] imageData;

}
