package com.memori.memori_service.dtos;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.memori.memori_domain.annotations.DiscriminatorValues;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "entityType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CardTagDto.class, name = DiscriminatorValues.CARD_TAG),
        @JsonSubTypes.Type(value = StudyOptionStateDto.class, name = DiscriminatorValues.STUDY_OPTION_STATE),
        @JsonSubTypes.Type(value = CardTagMappingDto.class, name = DiscriminatorValues.CARD_TAG_MAPPING),
        @JsonSubTypes.Type(value = UserRoleDto.class, name = DiscriminatorValues.USER_ROLE),
        @JsonSubTypes.Type(value = SyncEntityDto.class, name = DiscriminatorValues.SYNC_ENTITY),
        @JsonSubTypes.Type(value = DeckTagDto.class, name = DiscriminatorValues.DECK_TAG),
        @JsonSubTypes.Type(value = CardDto.class, name = DiscriminatorValues.CARD),
        @JsonSubTypes.Type(value = DeckTagMappingDto.class, name = DiscriminatorValues.DECK_TAG_MAPPING),
        @JsonSubTypes.Type(value = ReviewLogDto.class, name = DiscriminatorValues.REVIEW_LOG),
        @JsonSubTypes.Type(value = StudyOptionDto.class, name = DiscriminatorValues.STUDY_OPTION),
        @JsonSubTypes.Type(value = StudyOptionTagDto.class, name = DiscriminatorValues.STUDY_OPTION_TAG),
        @JsonSubTypes.Type(value = DeckImageDto.class, name = DiscriminatorValues.DECK_IMAGE),
        @JsonSubTypes.Type(value = CardHintDto.class, name = DiscriminatorValues.CARD_HINT),
        @JsonSubTypes.Type(value = DeckSettingsDto.class, name = DiscriminatorValues.DECK_SETTINGS),
        @JsonSubTypes.Type(value = UserDto.class, name = DiscriminatorValues.USER),
        @JsonSubTypes.Type(value = DeckDto.class, name = DiscriminatorValues.DECK),
        @JsonSubTypes.Type(value = StudyOptionDeckDto.class, name = DiscriminatorValues.STUDY_OPTION_DECK),
        @JsonSubTypes.Type(value = DeckReviewsDto.class, name = DiscriminatorValues.DECK_REVIEWS),
})
public class SyncEntityDto {

    private String id;

    private String createdAt;

    private String lastModified;

    private Long version;

    private String deletedAt;

    private String syncedAt;

    private String modifiedByDeviceId;

    private String userId;

    // private User user;

    private String entityType;

    public enum Action {
        CREATE(1),
        UPDATE(2),
        DELETE(3);

        private final int value;

        Action(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static boolean isValidCode(int value) {
            for (Action action : Action.values())
                if (action.getValue() == value)
                    return true;
            return false;
        }
    }

    private Action action;
}