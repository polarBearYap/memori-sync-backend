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
@DiscriminatorValue(DiscriminatorValues.DECK_TAG_MAPPING)
public class DeckTagMapping extends SyncEntity {

    @ManyToOne
    @JoinColumn(name = "deck_id", insertable = false, updatable = false)
    private Deck deck;

    @Column(name = "deck_id")
    private UUID deckId;

    @ManyToOne
    @JoinColumn(name = "deck_tag_id", insertable = false, updatable = false)
    private DeckTag deckTag;

    @Column(name = "deck_tag_id")
    private UUID deckTagId;

    public Integer getSortOrderConstant() {
        return 3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof DeckTagMapping))
            return false;
        if (!super.equals(o))
            return false;

        DeckTagMapping that = (DeckTagMapping) o;

        if (!deckId.equals(that.deckId))
            return false;
        if (!deckTagId.equals(that.deckTagId))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("Unimplemented method 'hashCode'");
    }
}
