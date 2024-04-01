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
@DiscriminatorValue(DiscriminatorValues.CARD_TAG_MAPPING)
public class CardTagMapping extends SyncEntity {

    @ManyToOne
    @JoinColumn(name = "card_id", insertable = false, updatable = false)
    private Card card;

    @Column(name = "card_id")
    private UUID cardId;

    @ManyToOne
    @JoinColumn(name = "card_tag_id", insertable = false, updatable = false)
    private CardTag cardTag;

    @Column(name = "card_tag_id")
    private UUID cardTagId;

    public Integer getSortOrderConstant() {
        return 4;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CardTagMapping))
            return false;
        if (!super.equals(o))
            return false;

        CardTagMapping that = (CardTagMapping) o;

        if (!cardId.equals(that.cardId))
            return false;
        if (!cardTagId.equals(that.cardTagId))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("Unimplemented method 'hashCode'");
    }
}