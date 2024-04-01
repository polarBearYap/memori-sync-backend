package com.memori.memori_domain;

import com.memori.memori_domain.annotations.DiscriminatorValues;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@NoArgsConstructor
@SuperBuilder
@DiscriminatorValue(DiscriminatorValues.DECK_SETTINGS)
public class DeckSettings extends SyncEntity {

    @Column(nullable = false)
    @Builder.Default
    private Boolean isDefault = false;

    @Column(nullable = false)
    @Builder.Default
    private String learningSteps = "1m 10m";

    @Column(nullable = false)
    @Builder.Default
    private String relearningSteps = "10m";

    @Column(nullable = false)
    @Builder.Default
    private Integer maxNewCardsPerDay = 20;

    @Column(nullable = false)
    @Builder.Default
    private Integer maxReviewPerDay = 200;

    @Column(nullable = false)
    @Builder.Default
    private Integer maximumAnswerSeconds = 60;

    @Column(nullable = false)
    @Builder.Default
    private Double desiredRetention = 0.95;

    public enum Priority {
        RANDOM(1),
        FIRST(2),
        SECOND(3),
        THIRD(4);

        private final int value;

        Priority(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static Priority fromValue(int value) {
            for (Priority priority : Priority.values()) {
                if (priority.getValue() == value) {
                    return priority;
                }
            }
            throw new IllegalArgumentException("No matching priority for value: " + value);
        }

        public static boolean isValidCode(int value) {
            for (Priority piority : Priority.values())
                if (piority.getValue() == value)
                    return true;
            return false;
        }
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    @Builder.Default
    private Priority newPriority = Priority.RANDOM;

    public void setNewPriority(int newPriority) {
        this.newPriority = Priority.fromValue(newPriority);
    }

    public int getNewPriority() {
        return newPriority.getValue();
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    @Builder.Default
    private Priority interdayPriority = Priority.RANDOM;

    public void setInterdayPriority(int interdayPriority) {
        this.interdayPriority = Priority.fromValue(interdayPriority);
    }

    public int getInterdayPriority() {
        return interdayPriority.getValue();
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    @Builder.Default
    private Priority reviewPriority = Priority.RANDOM;

    public void setReviewPriority(int reviewPriority) {
        this.reviewPriority = Priority.fromValue(reviewPriority);
    }

    public int getReviewPriority() {
        return reviewPriority.getValue();
    }

    public Integer getSortOrderConstant() {
        return 1;
    }

    @Column(nullable = false)
    @Builder.Default
    private Boolean skipNewCard = false;

    @Column(nullable = false)
    @Builder.Default
    private Boolean skipLearningCard = false;

    @Column(nullable = false)
    @Builder.Default
    private Boolean skipReviewCard = false;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof DeckSettings))
            return false;
        if (!super.equals(o))
            return false;

        DeckSettings that = (DeckSettings) o;

        if (!isDefault.equals(that.isDefault))
            return false;
        if (!learningSteps.equals(that.learningSteps))
            return false;
        if (!relearningSteps.equals(that.relearningSteps))
            return false;
        if (!maxNewCardsPerDay.equals(that.maxNewCardsPerDay))
            return false;
        if (!maxReviewPerDay.equals(that.maxReviewPerDay))
            return false;
        if (!maximumAnswerSeconds.equals(that.maximumAnswerSeconds))
            return false;
        if (!desiredRetention.equals(that.desiredRetention))
            return false;
        if (!newPriority.equals(that.newPriority))
            return false;
        if (!interdayPriority.equals(that.interdayPriority))
            return false;
        if (!reviewPriority.equals(that.reviewPriority))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("Unimplemented method 'hashCode'");
    }

}
