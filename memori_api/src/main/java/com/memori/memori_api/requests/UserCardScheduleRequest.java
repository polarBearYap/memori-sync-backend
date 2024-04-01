package com.memori.memori_api.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.memori.memori_api.validators.ValidCardState;
import com.memori.memori_api.validators.ValidIsoDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCardScheduleRequest {
    @JsonProperty("due")
    @NotNull
    @ValidIsoDateTime
    private String due;

    @JsonProperty("stability")
    @NotNull
    private Double stability;

    @JsonProperty("difficulty")
    @NotNull
    private Double difficulty;

    @JsonProperty("elapsed_days")
    @NotNull
    private Integer elapsedDays;

    @JsonProperty("scheduled_days")
    @NotNull
    private Integer scheduledDays;

    @JsonProperty("reps")
    @NotNull
    private Integer reps;

    @JsonProperty("lapses")
    @NotNull
    private Integer lapses;

    @JsonProperty("state")
    @NotNull
    @ValidCardState
    private Integer state;

    @JsonProperty("last_review")
    @NotNull
    @ValidIsoDateTime
    private String lastReview;

    @JsonProperty("current_review")
    @NotNull
    @ValidIsoDateTime
    private String currentReview;
}
