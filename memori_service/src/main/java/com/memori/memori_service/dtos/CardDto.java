package com.memori.memori_service.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CardDto extends SyncEntityDto {

    private String front;

    private String back;

    private String explanation;

    private Integer displayOrder;

    private Double difficulty;

    private String due;

    private String actual_due;

    private Integer elapsed_days;

    private Integer lapses;

    private String last_review;

    private Integer reps;

    private Integer scheduled_days;

    private Double stability;

    private Integer state;

    private Boolean isSuspended;

    private String deckId;

    // private DeckDto deck;
}
