package com.memori.memori_domain;

import java.util.List;

import com.memori.memori_domain.annotations.DiscriminatorValues;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@NoArgsConstructor
@SuperBuilder
@DiscriminatorValue(DiscriminatorValues.STUDY_OPTION)
public class StudyOption extends SyncEntity {

    public enum Mode {
        PREVIEW(1),
        STUDY(2);

        private final int value;

        Mode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static Mode fromValue(int value) {
            for (Mode mode : Mode.values()) {
                if (mode.getValue() == value) {
                    return mode;
                }
            }
            throw new IllegalArgumentException("No matching mode for value: " + value);
        }

        public static boolean isValidCode(int value) {
            for (Mode mode : Mode.values())
                if (mode.getValue() == value)
                    return true;
            return false;
        }
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    @Builder.Default
    private Mode mode = Mode.PREVIEW;

    public void setMode(int mode) {
        this.mode = Mode.fromValue(mode);
    }

    public int getMode() {
        return mode.getValue();
    }

    @OneToMany(mappedBy = "studyOption", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<StudyOptionTag> studyOptionTags;

    @OneToMany(mappedBy = "studyOption", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<StudyOptionState> studyOptionStates;

    @OneToMany(mappedBy = "studyOption", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<StudyOptionDeck> studyOptionDecks;

    public enum SortOption {
        RANDOM(1),
        MOST_DIFFICULT_FIRST(2),
        MOST_DIFFICULT_LAST(3),
        MOST_FORGOTTEN_FIRST(4),
        MOST_FORGOTTEN_LAST(5),
        EARLIEST_ADDED_FIRST(6),
        EARLIEST_ADDED_LAST(7),
        MOST_DUE_FIRST(8),
        MOST_DUE_LAST(9);

        private final int value;

        SortOption(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static SortOption fromValue(int value) {
            for (SortOption sortOption : SortOption.values()) {
                if (sortOption.getValue() == value) {
                    return sortOption;
                }
            }
            throw new IllegalArgumentException("No matching sortOption for value: " + value);
        }

        public static boolean isValidCode(int value) {
            for (SortOption option : SortOption.values())
                if (option.getValue() == value)
                    return true;
            return false;
        }
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    @Builder.Default
    private SortOption sortOption = SortOption.RANDOM;

    public void setSortOption(int sortOption) {
        this.sortOption = SortOption.fromValue(sortOption);
    }

    public int getSortOption() {
        return sortOption.getValue();
    }

    public Integer getSortOrderConstant() {
        return 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof StudyOption))
            return false;
        if (!super.equals(o))
            return false;

        StudyOption that = (StudyOption) o;

        if (!mode.equals(that.mode))
            return false;
        if (!sortOption.equals(that.sortOption))
            return false;
        // No check for the three attributes below
        // studyOptionTags;
        // studyOptionStates;
        // studyOptionDecks;
        return true;
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("Unimplemented method 'hashCode'");
    }
}
