package com.memori.memori_service.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CardTagMappingDto extends SyncEntityDto {

    private String cardId;

    private String cardTagId;

    // private CardDto card;

    // private CardTagDto cardTag;

}