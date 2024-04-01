package com.memori.memori_domain;

import java.util.UUID;

import com.memori.memori_domain.annotations.DiscriminatorValues;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@NoArgsConstructor
@SuperBuilder
@DiscriminatorValue(DiscriminatorValues.STUDY_OPTION_DECK)
public class StudyOptionDeck extends SyncEntity {

    @ManyToOne
    @JoinColumn(name = "deck_id", insertable = false, updatable = false)
    private Deck deck;

    @Column(name = "deck_id")
    private UUID deckId;

    @ManyToOne
    @JoinColumn(name = "study_option_id", insertable = false, updatable = false)
    private StudyOption studyOption;

    @Column(name = "study_option_id")
    private UUID studyOptionId;

    public Integer getSortOrderConstant() {
        return 3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof StudyOptionDeck))
            return false;
        if (!super.equals(o))
            return false;

        StudyOptionDeck that = (StudyOptionDeck) o;
        
        if (!deckId.equals(that.deckId))
            return false;
        if (!studyOptionId.equals(that.studyOptionId))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("Unimplemented method 'hashCode'");
    }
}