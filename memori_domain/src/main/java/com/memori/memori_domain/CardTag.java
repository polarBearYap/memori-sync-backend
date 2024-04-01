package com.memori.memori_domain;

import com.memori.memori_domain.annotations.DiscriminatorValues;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@NoArgsConstructor
@SuperBuilder
@DiscriminatorValue(DiscriminatorValues.CARD_TAG)
public class CardTag extends SyncEntity {

    @Column(nullable = false)
    private String name;

    public Integer getSortOrderConstant() {
        return 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CardTag))
            return false;
        if (!super.equals(o))
            return false;

        CardTag that = (CardTag) o;

        if (!name.equals(that.name))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("Unimplemented method 'hashCode'");
    }
}
