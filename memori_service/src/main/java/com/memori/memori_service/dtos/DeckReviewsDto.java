package com.memori.memori_service.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeckReviewsDto extends SyncEntityDto {

    private String comment;

    private Integer rating;

    private String deckId;
}
