package com.memori.memori_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewLogDto extends SyncEntityDto {

    private Integer elapsedDays;

    private Integer rating;

    private String review;

    private Integer scheduledDays;

    private Integer state;

    private Integer reviewDurationInMs;

    private String lastReview;

    private String cardId;

    // private CardDto card;

}
