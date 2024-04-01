package com.memori;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.memori.memori_domain.Card;
import com.memori.memori_domain.DeckSettings;
import com.memori.memori_domain.ReviewLog;
import com.memori.memori_domain.StudyOption;
import com.memori.memori_domain.User;
import com.memori.memori_domain.UserRole;
import com.memori.memori_domain.Card.State;
import com.memori.memori_domain.DeckSettings.Priority;
import com.memori.memori_domain.ReviewLog.Rating;
import com.memori.memori_domain.StudyOption.Mode;
import com.memori.memori_domain.StudyOption.SortOption;
import com.memori.memori_domain.User.Tier;
import com.memori.memori_domain.UserRole.Role;
import com.memori.memori_service.dtos.CardDto;
import com.memori.memori_service.dtos.DeckSettingsDto;
import com.memori.memori_service.dtos.ReviewLogDto;
import com.memori.memori_service.dtos.StudyOptionDto;
import com.memori.memori_service.dtos.UserDto;
import com.memori.memori_service.dtos.UserRoleDto;
import com.memori.memori_service.mappers.CardMapper;
import com.memori.memori_service.mappers.DeckSettingsMapper;
import com.memori.memori_service.mappers.ReviewLogMapper;
import com.memori.memori_service.mappers.StudyOptionMapper;
import com.memori.memori_service.mappers.UserMapper;
import com.memori.memori_service.mappers.UserRoleMapper;

public class MapperTest extends BaseTest {

    @Autowired
    private CardMapper cardMapper;

    @Autowired
    private DeckSettingsMapper deckSettingMapper;

    @Autowired
    private ReviewLogMapper reviewLogMapper;

    @Autowired
    private StudyOptionMapper studyOptionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Test
    void cardMapper_ShouldCorrect() {
        Instant iNow = Instant.now();
        OffsetDateTime oNow = OffsetDateTime.now(ZoneOffset.UTC);
        UUID dummyId = UUID.randomUUID();
        String userId = UUID.randomUUID().toString();
        Card card = Card.builder()
                .id(dummyId)
                .createdAt(iNow)
                .lastModified(iNow)
                .modifiedByDeviceId(dummyId)
                .syncedAt(iNow)
                .front("{\"a\":\"b\"}")
                .back("{\"c\":\"d\"}")
                .explanation("Explain!")
                .displayOrder(1)
                .difficulty(0.5)
                .due(oNow)
                .actual_due(oNow)
                .elapsed_days(5)
                .lapses(5)
                .last_review(iNow)
                .reps(1)
                .scheduled_days(666)
                .stability(1.5)
                .state(State.LEARNING)
                .isSuspended(false)
                .deckId(UUID.randomUUID())
                .deletedAt(null)
                .version(0L)
                .userId(userId)
                .build();
        card.setSortOrder(card.getSortOrderConstant());

        CardDto dto = cardMapper.entityToDto(card);

        assertTrue(card.getId().toString().equals(dto.getId()));
        assertTrue(card.getDeckId().toString().equals(dto.getDeckId()));

        // Validate CardStateMapper.class (Positive scenario)
        assertTrue(dto.getState().equals(card.getState()));
        card.setSortOrder(card.getSortOrderConstant());


        // Validate CardStateMapper.class && DateTimeMapper.class (Positive scenario)
        // Ensure that offset date time and instant is serialized and deserialized
        // correctly
        Card card2 = cardMapper.dtoToEntity(dto);
        card2.setSortOrder(card2.getSortOrderConstant());
        assertTrue(card.equals(card2));

        // Validate CardStateMapper.class (Negative scenario)
        // Ensure the invalid card state is not accepted
        dto.setState(999);
        assertThrows(IllegalArgumentException.class, () -> {
            cardMapper.dtoToEntity(dto);
        });
        dto.setState(1);

        // Validate DateTimeMapper.class (Positive edge & Negative scenario)
        // Ensure the alternative local offset date time is accepted
        dto.setDue(oNow.withOffsetSameInstant(ZoneOffset.ofHours(8)).format(DateTimeFormatter.ISO_INSTANT));
        card2 = cardMapper.dtoToEntity(dto);
        assertTrue(OffsetDateTime.parse(dto.getDue()).equals(card2.getDue()));

        // Ensure the alternative instant is accepted
        dto.setCreatedAt(iNow.atOffset(ZoneOffset.ofHours(8)).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        card2 = cardMapper.dtoToEntity(dto);
        assertTrue(Instant.parse(dto.getCreatedAt()).equals(card2.getCreatedAt()));

        // Ensure the invalid offset date time is not accepted
        dto.setDue("2024-02-16T13:18:39");
        assertThrows(DateTimeParseException.class, () -> {
            cardMapper.dtoToEntity(dto);
        });
        dto.setDue("2024-02-16T13:18:39Z");

        // Ensure the invalid instant is not accepted
        dto.setCreatedAt("2024-02-16");
        assertThrows(DateTimeParseException.class, () -> {
            cardMapper.dtoToEntity(dto);
        });
    }

    @Test
    void reviewLog_ShouldCorrect() {
        Instant iNow = Instant.now();
        UUID dummyId = UUID.randomUUID();
        String userId = UUID.randomUUID().toString();
        ReviewLog reviewLog = ReviewLog.builder()
                .id(dummyId)
                .createdAt(iNow)
                .lastModified(iNow)
                .modifiedByDeviceId(dummyId)
                .syncedAt(iNow)
                .state(State.NEW)
                .deletedAt(null)
                .version(0L)
                .userId(userId)
                .elapsedDays(5)
                .scheduledDays(5)
                .rating(Rating.GOOD)
                .review(iNow)
                .lastReview(iNow)
                .reviewDurationInMs(5000)
                .cardId(dummyId)
                .build();

        ReviewLogDto dto = reviewLogMapper.entityToDto(reviewLog);

        assertTrue(reviewLog.getId().toString().equals(dto.getId()));
        assertTrue(reviewLog.getCardId().toString().equals(dto.getCardId()));

        // Validate CardRatingMapper.class (Positive scenario)
        assertTrue(dto.getRating().equals(reviewLog.getRating()));

        // Validate CardRatingMapper.class (Negative scenario)
        dto.setRating(999);
        assertThrows(IllegalArgumentException.class, () -> {
            reviewLogMapper.dtoToEntity(dto);
        });
    }

    @Test
    void deckSettings_ShouldCorrect() {
        Instant iNow = Instant.now();
        UUID dummyId = UUID.randomUUID();
        String userId = UUID.randomUUID().toString();
        DeckSettings deckSettings = DeckSettings.builder()
                .id(dummyId)
                .createdAt(iNow)
                .lastModified(iNow)
                .modifiedByDeviceId(dummyId)
                .syncedAt(iNow)
                .deletedAt(null)
                .version(0L)
                .userId(userId)
                .newPriority(Priority.SECOND)
                .build();
        DeckSettingsDto dto = deckSettingMapper.entityToDto(deckSettings);

        assertTrue(deckSettings.getId().toString().equals(dto.getId()));

        // Validate DeckPriorityMapper.class (Positive scenario)
        assertTrue(dto.getNewPriority().equals(deckSettings.getNewPriority()));

        // Validate DeckPriorityMapper.class (Negative scenario)
        dto.setNewPriority(999);
        assertThrows(IllegalArgumentException.class, () -> {
            deckSettingMapper.dtoToEntity(dto);
        });
    }

    @Test
    void studyoption_ShouldCorrect() {
        Instant iNow = Instant.now();
        UUID dummyId = UUID.randomUUID();
        String userId = UUID.randomUUID().toString();
        StudyOption studyOption = StudyOption.builder()
                .id(dummyId)
                .createdAt(iNow)
                .lastModified(iNow)
                .modifiedByDeviceId(dummyId)
                .syncedAt(iNow)
                .deletedAt(null)
                .version(0L)
                .userId(userId)
                .mode(Mode.STUDY)
                .sortOption(SortOption.MOST_DUE_FIRST)
                .build();
        StudyOptionDto dto = studyOptionMapper.entityToDto(studyOption);

        assertTrue(studyOption.getId().toString().equals(dto.getId()));

        // Validate StudyOptionModeMapper.class (Positive scenario)
        assertTrue(dto.getMode().equals(studyOption.getMode()));
        // Validate StudySortOptionMapper.class (Positive scenario)
        assertTrue(dto.getSortOption().equals(studyOption.getSortOption()));

        // Validate StudyOptionModeMapper.class (Negative scenario)
        dto.setMode(999);
        assertThrows(IllegalArgumentException.class, () -> {
            studyOptionMapper.dtoToEntity(dto);
        });
        // Validate StudySortOptionMapper.class (Negative scenario)
        dto.setMode(1);
        dto.setSortOption(999);
        assertThrows(IllegalArgumentException.class, () -> {
            studyOptionMapper.dtoToEntity(dto);
        });
    }

    @Test
    void userMapper_ShouldCorrect() {
        String userId = UUID.randomUUID().toString();
        User user = User.builder()
                .id(userId)
                .email("testtest@gmail.com")
                .username("testtest")
                .tier(Tier.PRO)
                .build();
        UserDto dto = userMapper.entityToDto(user);

        assertTrue(user.getId().toString().equals(dto.getId()));

        // Validate UserTierMapper.class (Positive scenario)
        assertTrue(dto.getTier().equals(user.getTier()));

        // Validate UserTierMapper.class (Negative scenario)
        dto.setTier(666);
        assertThrows(IllegalArgumentException.class, () -> {
            userMapper.dtoToEntity(dto);
        });
    }

    @Test
    void userRoleMapper_ShouldCorrect() {
        String userId = UUID.randomUUID().toString();
        UserRole userRole = UserRole.builder()
                .id(666L)
                .userId(userId)
                .role(Role.ADMIN)
                .build();
        UserRoleDto dto = userRoleMapper.entityToDto(userRole);

        assertTrue(userRole.getId().equals(dto.getId()));

        // Validate UserRoleEnumMapper.class (Positive scenario)
        assertTrue(dto.getRole().equals(userRole.getRole()));

        // Validate UserRoleEnumMapper.class (Negative scenario)
        dto.setRole(999);
        assertThrows(IllegalArgumentException.class, () -> {
            userRoleMapper.dtoToEntity(dto);
        });
    }
}
