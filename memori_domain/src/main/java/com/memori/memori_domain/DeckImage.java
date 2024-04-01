package com.memori.memori_domain;

import java.util.UUID;

import com.memori.memori_domain.annotations.DiscriminatorValues;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@NoArgsConstructor
@SuperBuilder
@DiscriminatorValue(DiscriminatorValues.DECK_IMAGE)
public class DeckImage extends SyncEntity {

    @ManyToOne
    @JoinColumn(name = "deck_id", insertable = false, updatable = false)
    private Deck deck;

    @Column(name = "deck_id")
    private UUID deckId;

    /*
     * Storage Efficiency: Storing images directly in the database can make backups and migrations slower and more cumbersome. 
     * It's often more efficient to store images in a file system or a dedicated object storage service (like Amazon S3, Google Cloud Storage, or Azure Blob Storage) 
     * and save the file path or URL in the database instead.
     */
    @Lob
    private byte[] imageData;

    public Integer getSortOrderConstant() {
        return 3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof DeckImage))
            return false;
        if (!super.equals(o))
            return false;

        DeckImage that = (DeckImage) o;

        // Warning: Skip check on image data for performance reason
        if (!deckId.equals(that.deckId))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("Unimplemented method 'hashCode'");
    }
}
