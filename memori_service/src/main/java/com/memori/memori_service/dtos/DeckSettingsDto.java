package com.memori.memori_service.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeckSettingsDto extends SyncEntityDto {

    private Boolean isDefault;

    private String learningSteps;

    private String relearningSteps;

    private Integer maxNewCardsPerDay;

    private Integer maxReviewPerDay;

    private Integer maximumAnswerSeconds;

    private Double desiredRetention;

    private Integer newPriority;

    private Integer interdayPriority;

    private Integer reviewPriority;

    private Boolean skipNewCard;

    private Boolean skipLearningCard;

    private Boolean skipReviewCard;
}
