package com.memori.memori_service.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CardHintDto extends SyncEntityDto {

    private Integer displayOrder = 1;

    private String text;

    private String cardId;

    // private CardDto card;
}