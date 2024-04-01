package com.memori.memori_domain;

import java.time.Instant;
import java.time.OffsetDateTime;
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
@DiscriminatorValue(DiscriminatorValues.CARD)
public class Card extends SyncEntity {

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false)
    private String front;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false)
    private String back;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false)
    private String explanation;

    @Column(nullable = false)
    private Integer displayOrder;

    @Column(nullable = false)
    private Double difficulty;

    @Column(nullable = false)
    private OffsetDateTime due;

    @Column(nullable = false)
    private OffsetDateTime actual_due;

    @Column(nullable = false)
    private Integer elapsed_days;

    @Column(nullable = false)
    private Integer lapses;

    @Column(nullable = false)
    private Instant last_review;

    @Column(nullable = false)
    private Integer reps;

    @Column(nullable = false)
    private Integer scheduled_days;

    @Column(nullable = false)
    private Double stability;

    public enum State {
        NEW(0),
        LEARNING(1),
        REVIEW(2),
        RELEARNING(3);

        private final int value;

        State(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static State fromValue(int value) {
            for (State state : State.values()) {
                if (state.getValue() == value) {
                    return state;
                }
            }
            throw new IllegalArgumentException("No matching state for value: " + value);
        }

        public static boolean isValidCode(int value) {
            for (State state : State.values())
                if (state.getValue() == value)
                    return true;
            return false;
        }
    }

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
    private Boolean isSuspended;

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
        if (!(o instanceof Card))
            return false;
        if (!super.equals(o))
            return false;

        Card that = (Card) o;
        
        if (!front.equals(that.front))
            return false;
        if (!back.equals(that.back))
            return false;
        if (!explanation.equals(that.explanation))
            return false;
        if (!displayOrder.equals(that.displayOrder))
            return false;
        if (!difficulty.equals(that.difficulty))
            return false;
        if (!due.equals(that.due))
            return false;
        if (!actual_due.equals(that.actual_due))
            return false;
        if (!elapsed_days.equals(that.elapsed_days))
            return false;
        if (!lapses.equals(that.lapses))
            return false;
        if (!last_review.equals(that.last_review))
            return false;
        if (!reps.equals(that.reps))
            return false;
        if (!scheduled_days.equals(that.scheduled_days))
            return false;
        if (!stability.equals(that.stability))
            return false;
        if (!state.equals(that.state))
            return false;
        if (!isSuspended.equals(that.isSuspended))
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
