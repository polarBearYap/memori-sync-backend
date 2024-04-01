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
@DiscriminatorValue(DiscriminatorValues.STUDY_OPTION_TAG)
public class StudyOptionTag extends SyncEntity {

    @ManyToOne
    @JoinColumn(name = "card_tag_id", insertable = false, updatable = false)
    private CardTag cardTag;

    @Column(name = "card_tag_id")
    private UUID cardTagId;

    @ManyToOne
    @JoinColumn(name = "study_option_id", insertable = false, updatable = false)
    private StudyOption studyOption;

    @Column(name = "study_option_id")
    private UUID studyOptionId;

    public Integer getSortOrderConstant() {
        return 2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof StudyOptionTag))
            return false;
        if (!super.equals(o))
            return false;

        StudyOptionTag that = (StudyOptionTag) o;
        
        if (!cardTagId.equals(that.cardTagId))
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