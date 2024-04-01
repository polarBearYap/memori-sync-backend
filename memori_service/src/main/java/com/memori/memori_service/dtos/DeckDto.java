package com.memori.memori_service.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeckDto extends SyncEntityDto {

    private String name;

    private String description;

    private Integer totalCount = 0;

    private Integer newCount = 0;

    private Integer learningCount = 0;

    private Integer reviewCount = 0;

    private String shareCode = "";

    private Boolean canShareExpired = false;

    private String shareExpirationTime;

    private String deckSettingsId;

    private String lastReviewTime;

    // private DeckSettingsDto deckSettings;
}
