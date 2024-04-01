package com.memori.memori_api.responses;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.memori.memori_api.validators.ValidCardRating;
import com.memori.memori_api.validators.ValidCardState;
import com.memori.memori_api.validators.ValidIsoDateTime;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCardScheduleResponse {
    @JsonProperty("message")
    @NotNull
    private String message;

    @JsonProperty("data")
    @Valid
    @NotNull
    private Data data;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonSerialize(using = DataSerializer.class)
    @JsonDeserialize(using = DataDeserializer.class)
    public static class Data {
        @Valid
        @NotNull
        private Map<Integer, DataItem> items;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataItem {
        @JsonProperty("card")
        @Valid
        @NotNull
        private Card card;

        @JsonProperty("review_log")
        @Valid
        @NotNull
        private ReviewLog reviewLog;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Card {

        @JsonProperty("difficulty")
        @NotNull
        private Double difficulty;

        @JsonProperty("due")
        @NotNull
        @ValidIsoDateTime
        private String due;

        @JsonProperty("elapsed_days")
        @NotNull
        private Integer elapsedDays;

        @JsonProperty("lapses")
        @NotNull
        private Integer lapses;

        @JsonProperty("last_review")
        @NotNull
        @ValidIsoDateTime
        private String lastReview;

        @JsonProperty("reps")
        @NotNull
        private Integer reps;

        @JsonProperty("scheduled_days")
        @NotNull
        private Integer scheduledDays;

        @JsonProperty("stability")
        @NotNull
        private Double stability;

        @JsonProperty("state")
        @NotNull
        @ValidCardState
        private Integer state;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewLog {

        @JsonProperty("elapsed_days")
        @NotNull
        private Integer elapsedDays;

        @JsonProperty("rating")
        @NotNull
        @ValidCardRating
        private Integer rating;

        @JsonProperty("review")
        @NotNull
        @ValidIsoDateTime
        private String review;

        @JsonProperty("scheduled_days")
        @NotNull
        private Integer scheduledDays;

        @JsonProperty("state")
        @NotNull
        @ValidCardState
        private Integer state;
    }
}