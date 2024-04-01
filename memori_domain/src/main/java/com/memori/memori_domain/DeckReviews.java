package com.memori.memori_domain;

import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
@DiscriminatorValue(DiscriminatorValues.DECK_REVIEWS)
public class DeckReviews extends SyncEntity {

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false)
    private String comment;

    public enum Rating {
        ONE(1),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5);

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
    private Rating rating = Rating.FIVE;

    public void setRating(int rating) {
        this.rating = Rating.fromValue(rating);
    }

    public int getRating() {
        return rating.getValue();
    }

    @ManyToOne
    @JoinColumn(name = "deck_id", insertable = false, updatable = false)
    private Deck deck;

    @Column(name = "deck_id")
    private UUID deckId;

    public Integer getSortOrderConstant() {
        return 3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof DeckReviews))
            return false;
        if (!super.equals(o))
            return false;

        DeckReviews that = (DeckReviews) o;

        if (!comment.equals(that.comment))
            return false;
        if (!rating.equals(that.rating))
            return false;
        if (!deckId.equals(that.deckId))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("Unimplemented method 'hashCode'");
    }
}
