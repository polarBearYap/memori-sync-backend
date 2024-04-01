package com.memori.memori_domain;

import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.memori.memori_domain.annotations.DiscriminatorValues;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
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
@DiscriminatorValue(DiscriminatorValues.CARD_HINT)
public class CardHint extends SyncEntity {

    @Column(nullable = false)
    @Builder.Default
    private Integer displayOrder = 1;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false)
    private String text;

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
        if (!(o instanceof CardHint))
            return false;
        if (!super.equals(o))
            return false;

        CardHint that = (CardHint) o;

        if (displayOrder.equals(that.displayOrder))
            return false;
        if (text.equals(that.text))
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