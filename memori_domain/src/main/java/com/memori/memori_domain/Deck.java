package com.memori.memori_domain;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

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
@DiscriminatorValue(DiscriminatorValues.DECK)
public class Deck extends SyncEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Builder.Default
    private Integer totalCount = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer newCount = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer learningCount = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer reviewCount = 0;

    @Column(nullable = false)
    @Builder.Default
    private String shareCode = "";

    @Column(nullable = false)
    @Builder.Default
    private Boolean canShareExpired = false;

    @Column(nullable = false)
    private OffsetDateTime shareExpirationTime;

    @ManyToOne
    @JoinColumn(name = "deck_settings_id", insertable = false, updatable = false)
    private DeckSettings deckSettings;

    @Column(name = "deck_settings_id")
    private UUID deckSettingsId;

    public Integer getSortOrderConstant() {
        return 2;
    }

    @Column(nullable = true)
    private Instant lastReviewTime;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Deck))
            return false;
        if (!super.equals(o))
            return false;

        Deck that = (Deck) o;

        if (!name.equals(that.name))
            return false;
        if (!description.equals(that.description))
            return false;
        if (!totalCount.equals(that.totalCount))
            return false;
        if (!newCount.equals(that.newCount))
            return false;
        if (!learningCount.equals(that.learningCount))
            return false;
        if (!reviewCount.equals(that.reviewCount))
            return false;
        if (!shareCode.equals(that.shareCode))
            return false;
        if (!canShareExpired.equals(that.canShareExpired))
            return false;
        if (!shareExpirationTime.equals(that.shareExpirationTime))
            return false;
        if (!lastReviewTime.equals(that.lastReviewTime))
            return false;
        if (!deckSettingsId.equals(that.deckSettingsId))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("Unimplemented method 'hashCode'");
    }
}
