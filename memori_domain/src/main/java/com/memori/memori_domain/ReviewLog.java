package com.memori.memori_domain;

import java.time.Instant;
import java.util.UUID;

import com.memori.memori_domain.Card.State;
import com.memori.memori_domain.annotations.DiscriminatorValues;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@NoArgsConstructor
@SuperBuilder
@DiscriminatorValue(DiscriminatorValues.REVIEW_LOG)
public class ReviewLog extends SyncEntity {

    @Column(nullable = false)
    private Integer elapsedDays;

    public enum Rating {
        AGAIN(1),
        HARD(2),
        GOOD(3),
        EASY(4);

        private final int value;

        Rating(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static Rating fromValue(int value) {
            for (Rating rating : Rating.values()) {
                if (rating.getValue() == value) {
                    return rating;
                }
            }
            throw new IllegalArgumentException("No matching rating for value: " + value);
        }

        public static boolean isValidCode(int value) {
            for (Rating rating : Rating.values())
                if (rating.getValue() == value)
                    return true;
            return false;
        }
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    @Builder.Default
    private Rating rating = Rating.GOOD;

    public void setRating(int rating) {
        this.rating = Rating.fromValue(rating);
    }

    public int getRating() {
        return rating.getValue();
    }

    @Column(nullable = false)
    private Instant review;

    @Column(nullable = false)
    private Integer scheduledDays;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    @Builder.Default
    private State state = State.NEW;

    public void setState(int state) {
        this.state = State.fromValue(state);
    }

    public int getState() {
        return state.getValue();
    }

    @Column(nullable = false)
    private Integer reviewDurationInMs;

    @Column(nullable = false)
    private Instant lastReview;

    @ManyToOne
    @JoinColumn(name = "card_id", insertable = false, updatable = false)
    private Card card;

    @Column(name = "card_id")
    private UUID cardId;

    public Integer getSortOrderConstant() {
        return 4;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ReviewLog))
            return false;
        if (!super.equals(o))
            return false;

        ReviewLog that = (ReviewLog) o;

        if (!elapsedDays.equals(that.elapsedDays))
            return false;
        if (!rating.equals(that.rating))
            return false;
        if (!review.equals(that.review))
            return false;
        if (!scheduledDays.equals(that.scheduledDays))
            return false;
        if (!state.equals(that.state))
            return false;
        if (!reviewDurationInMs.equals(that.reviewDurationInMs))
            return false;
        if (!lastReview.equals(that.lastReview))
            return false;
        if (!cardId.equals(that.cardId))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("Unimplemented method 'hashCode'");
    }
}
