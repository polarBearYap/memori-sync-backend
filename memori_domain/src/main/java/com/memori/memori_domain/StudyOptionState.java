package com.memori.memori_domain;

import java.util.UUID;

import com.memori.memori_domain.Card.State;
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
@DiscriminatorValue(DiscriminatorValues.STUDY_OPTION_STATE)
public class StudyOptionState extends SyncEntity {

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
        if (!(o instanceof StudyOptionState))
            return false;
        if (!super.equals(o))
            return false;

        StudyOptionState that = (StudyOptionState) o;
        
        if (!state.equals(that.state))
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
