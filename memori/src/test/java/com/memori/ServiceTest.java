package com.memori;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.memori.memori_service.dtos.DeckSettingsDto;
import com.memori.memori_service.dtos.PaginatedDto;
import com.memori.memori_service.dtos.PullSyncResponseDto;
import com.memori.memori_service.dtos.StudyOptionDto;
import com.memori.memori_service.dtos.SyncEntityDto;
import com.memori.memori_service.dtos.SyncEntityDto.Action;
import com.memori.memori_service.dtos.UserDto;
import com.memori.memori_service.services.GeneralSyncService;
import com.memori.memori_service.services.UserService;

import jakarta.persistence.EntityNotFoundException;

public class ServiceTest extends BaseTest {
    @Autowired
    GeneralSyncService generalSyncService;

    @Autowired
    UserService userService;

    @Test
    @Order(1)
    void generalSyncService_Reflection_ShouldNoException() {
        List<SyncEntityDto> requests = new ArrayList<>();

        OffsetDateTime oNext = OffsetDateTime.now(ZoneOffset.UTC);
        String oNextStr = oNext.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        OffsetDateTime onFinal = oNext.plusHours(1);
        String onFinalStr = onFinal.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        UserDto userDto = createUser("servicetest1@gmail.com", "servicetest1");
        String userId = userDto.getId();

        DeckSettingsDto ds = createDefaultDeckSettings();
        ds.setId(UUID.randomUUID().toString());
        ds.setAction(Action.CREATE);
        ds.setUserId(userId);
        requests.add(ds);

        StudyOptionDto so = createDefaultStudyOptionDto();
        String studyOptionId = UUID.randomUUID().toString();
        so.setId(studyOptionId);
        so.setAction(Action.CREATE);
        so.setUserId(userId);
        requests.add(so);

        // Positive scenario: Entity creation should no return conflict rows
        PullSyncResponseDto dtos = generalSyncService.normalPush(requests);
        assertTrue(dtos.getConflictedItems().isEmpty());

        so = getStudyOption(studyOptionId);
        so.setAction(Action.UPDATE);
        so.setMode(1);
        so.setSortOption(2);
        so.setLastModified(oNextStr);
        requests.clear();
        requests.add(so);

        // Positive scenario: Entity update should no return conflict rows
        dtos = generalSyncService.normalPush(requests);
        assertTrue(dtos.getConflictedItems().isEmpty());

        so = getStudyOption(studyOptionId);
        so.setAction(Action.DELETE);
        so.setLastModified(onFinalStr);
        so.setDeletedAt(onFinalStr);
        requests.clear();
        requests.add(so);

        // Positive scenario: Entity delete should no return conflict rows
        dtos = generalSyncService.normalPush(requests);
        assertTrue(dtos.getConflictedItems().isEmpty());
    }

    @Test
    @Order(2)
    void generalSyncService_Conflict_ShouldReturnRows() {
        List<SyncEntityDto> requests = new ArrayList<>();

        OffsetDateTime onNext = OffsetDateTime.now(ZoneOffset.UTC);
        String oNextStr = onNext.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        StudyOptionDto so = createDefaultStudyOptionDto();

        String studyOptionId = UUID.randomUUID().toString();
        so.setId(studyOptionId);
        so.setAction(Action.CREATE);
        requests.add(so);

        assertDoesNotThrow(() -> {
            PullSyncResponseDto dtos = generalSyncService.normalPush(requests);
            assertTrue(dtos.getConflictedItems().isEmpty());
        }, "No error should be thrown");

        // Positive scenario: Entity conflicted update should return conflict rows
        so = getStudyOption(studyOptionId);
        so.setAction(Action.UPDATE);
        so.setMode(1);
        so.setSortOption(2);
        so.setLastModified(oNextStr);
        so.setVersion(so.getVersion() - 1);
        requests.clear();
        requests.add(so);

        PullSyncResponseDto dtos = generalSyncService.normalPush(requests);
        assertTrue(dtos.getConflictedItems().size() == 1);
        assertTrue(dtos.getConflictedItems().get(0).getId().equals(so.getId()));

        // Positive scenario: Entity conflicted delete should return conflict rows
        so.setAction(Action.DELETE);
        so.setDeletedAt(oNextStr);
        dtos = generalSyncService.normalPush(requests);
        assertTrue(dtos.getConflictedItems().size() == 1);
        assertTrue(dtos.getConflictedItems().get(0).getId().equals(so.getId()));
    }

    @Test
    @Order(3)
    void generalSyncService_OverrideConflict_ShouldNotReturnRows() {
        List<SyncEntityDto> requests = new ArrayList<>();

        OffsetDateTime onNext = OffsetDateTime.now(ZoneOffset.UTC);
        String oNextStr = onNext.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        StudyOptionDto so = createDefaultStudyOptionDto();

        String studyOptionId = UUID.randomUUID().toString();
        so.setId(studyOptionId);
        so.setAction(Action.CREATE);
        requests.add(so);

        assertDoesNotThrow(() -> {
            PullSyncResponseDto dtos = generalSyncService.normalPush(requests);
            assertTrue(dtos.getConflictedItems().isEmpty());
        }, "No error should be thrown");

        // Positive scenario: Entity override conflicted update should not return
        // conflict rows
        so = getStudyOption(studyOptionId);
        so.setAction(Action.UPDATE);
        so.setMode(1);
        so.setSortOption(2);
        so.setLastModified(oNextStr);
        so.setVersion(so.getVersion() - 1);
        requests.clear();
        requests.add(so);

        PullSyncResponseDto dtos = generalSyncService.overridePush(requests);
        assertTrue(dtos.getConflictedItems().size() == 0);

        // Positive scenario: Entity override conflicted delete should not return
        // conflict rows
        so.setAction(Action.DELETE);
        so.setDeletedAt(oNextStr);
        so.setVersion(so.getVersion() - 1);
        dtos = generalSyncService.overridePush(requests);
        assertTrue(dtos.getConflictedItems().size() == 0);
    }

    @Test
    @Order(4)
    void generalSyncService_ShouldReturnRowsToSync() {
        OffsetDateTime oPast = OffsetDateTime.now(ZoneOffset.UTC);
        String oPastStr = oPast.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        OffsetDateTime oNow = oPast.plusMinutes(5);
        String oNowStr = oNow.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        OffsetDateTime oBetween = oNow.plusMinutes(5);
        String oBetweenStr = oBetween.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        OffsetDateTime oLater = oNow.plusHours(1);
        String oLaterStr = oLater.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        UserDto firstUser = createUser("firstuser2@gmail.com", "firstuser2");
        String userId = firstUser.getId();
        UserDto secondUser = createUser("seconduser2@gmail.com", "seconduser2");

        // ------------------------------------------------------------------------------------------------
        // Scenario #1: syncCreation return empty record
        List<SyncEntityDto> res = generalSyncService.pullCreation(oNowStr, userId, 0, 50).getContent();
        assertTrue(res.size() == 0);

        // Scenario #2: syncCreation return all newly created record and record is
        // user-specific
        DeckSettingsDto ds = createDefaultDeckSettings();
        ds.setId(UUID.randomUUID().toString());
        ds.setAction(Action.CREATE);
        ds.setUserId(userId);
        ds.setCreatedAt(oNowStr);
        res.add(ds);

        StudyOptionDto so = createDefaultStudyOptionDto();
        so.setId(UUID.randomUUID().toString());
        so.setAction(Action.CREATE);
        so.setUserId(secondUser.getId());
        so.setCreatedAt(oLaterStr);
        res.add(so);

        StudyOptionDto so2 = createDefaultStudyOptionDto();
        so2.setId(UUID.randomUUID().toString());
        so2.setAction(Action.CREATE);
        so2.setUserId(userId);
        so2.setCreatedAt(oLaterStr);
        res.add(so2);

        PullSyncResponseDto pushRes = generalSyncService.normalPush(res);
        assertTrue(pushRes.getConflictedItems().size() == 0);

        res = generalSyncService.pullCreation(oPastStr, userId, 0, 50).getContent();
        assertTrue(res.size() == 2);
        for (SyncEntityDto r : res) {
            assertTrue(r.getUserId().equals(userId));
            // Special scenario: Ensure the function return the subtype of sync entity
            assertTrue(r instanceof DeckSettingsDto || r instanceof StudyOptionDto);
        }

        // Scenario #3: syncCreation return all newly created record since a specific
        // synced date
        res = generalSyncService.pullCreation(oBetweenStr, userId, 0, 50).getContent();
        assertTrue(res.size() == 1);

        // Scenario #4: syncCreation return paginated record, pagination is correct
        PaginatedDto<SyncEntityDto> dto = generalSyncService.pullCreation(oPastStr, userId, 0, 1);
        assertTrue(dto.getContent().size() == 1);
        assertTrue(dto.getTotalPages().equals(2));
        assertTrue(dto.getTotalElements().equals(2L));
        assertTrue(dto.getCurrentPageNumber().equals(0));
        assertTrue(dto.getHasPreviousPage().equals(false));
        assertTrue(dto.getHasNextPage().equals(true));
        dto =  generalSyncService.pullCreation(oPastStr, userId, 1, 1);
        assertTrue(dto.getContent().size() == 1);
        assertTrue(dto.getTotalPages().equals(2));
        assertTrue(dto.getTotalElements().equals(2L));
        assertTrue(dto.getCurrentPageNumber().equals(1));
        assertTrue(dto.getHasPreviousPage().equals(true));
        assertTrue(dto.getHasNextPage().equals(false));
        // ------------------------------------------------------------------------------------------------

        // ------------------------------------------------------------------------------------------------
        // Scenario #1: syncUpdate return empty record
        oPast = oLater.plusMinutes(5);
        oPastStr = oPast.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        oNow = oPast.plusMinutes(5);
        oNowStr = oNow.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        oBetween = oNow.plusMinutes(5);
        oBetweenStr = oBetween.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        oLater = oNow.plusHours(1);
        oLaterStr = oLater.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        res = generalSyncService.pullUpdate(oPastStr, userId, 0, 50).getContent();
        assertTrue(res.size() == 0);

        // Scenario #2: syncUpdate return newly created record and record is
        // user-specific
        ds = getDeckSettingsDto(ds.getId());
        ds.setAction(Action.UPDATE);
        ds.setLastModified(oNowStr);
        ds.setDesiredRetention(0.99);
        res.add(ds);

        so = getStudyOption(so.getId());
        so.setAction(Action.UPDATE);
        so.setLastModified(oLaterStr);
        so.setSortOption(2);
        res.add(so);

        so2 = getStudyOption(so2.getId());
        so2.setAction(Action.UPDATE);
        so2.setLastModified(oLaterStr);
        so.setSortOption(2);
        res.add(so2);

        pushRes = generalSyncService.normalPush(res);
        assertTrue(pushRes.getConflictedItems().size() == 0);

        res = generalSyncService.pullUpdate(oPastStr, userId, 0, 50).getContent();
        assertTrue(res.size() == 2);
        for (SyncEntityDto r : res) {
            assertTrue(r.getUserId().equals(userId));
            // Special scenario: Ensure the function return the subtype of sync entity
            assertTrue(r instanceof DeckSettingsDto || r instanceof StudyOptionDto);
        }

        // Scenario #3: syncUpdate return all newly created record since a specific
        // synced date
        res = generalSyncService.pullUpdate(oBetweenStr, userId, 0, 50).getContent();
        assertTrue(res.size() == 1);

        // Scenario #4: syncUpdate return paginated record, pagination is correct
        res = generalSyncService.pullUpdate(oPastStr, userId, 0, 1).getContent();
        assertTrue(res.size() == 1);
        res = generalSyncService.pullUpdate(oPastStr, userId, 1, 1).getContent();
        assertTrue(res.size() == 1);
        // ------------------------------------------------------------------------------------------------

        // ------------------------------------------------------------------------------------------------
        // Scenario #1: syncDelete return empty record
        oPast = oLater.plusMinutes(5);
        oPastStr = oPast.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        oNow = oPast.plusMinutes(5);
        oNowStr = oNow.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        oBetween = oNow.plusMinutes(5);
        oBetweenStr = oBetween.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        oLater = oNow.plusHours(1);
        oLaterStr = oLater.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        res = generalSyncService.pullDeletion(oPastStr, userId, 0, 50).getContent();
        assertTrue(res.size() == 0);

        // Scenario #2: syncDelete return newly created record and record is
        // user-specific
        ds = getDeckSettingsDto(ds.getId());
        ds.setAction(Action.DELETE);
        ds.setLastModified(oNowStr);
        ds.setDeletedAt(oNowStr);
        res.add(ds);

        so = getStudyOption(so.getId());
        so.setAction(Action.DELETE);
        so.setLastModified(oLaterStr);
        so.setDeletedAt(oLaterStr);
        res.add(so);

        so2 = getStudyOption(so2.getId());
        so2.setAction(Action.DELETE);
        so2.setLastModified(oLaterStr);
        so2.setDeletedAt(oLaterStr);
        res.add(so2);

        pushRes = generalSyncService.normalPush(res);
        assertTrue(pushRes.getConflictedItems().size() == 0);

        res = generalSyncService.pullDeletion(oPastStr, userId, 0, 50).getContent();
        assertTrue(res.size() == 2);
        for (SyncEntityDto r : res) {
            assertTrue(r.getUserId().equals(userId));
            // Special scenario: Ensure the function return the subtype of sync entity
            assertTrue(r instanceof DeckSettingsDto || r instanceof StudyOptionDto);
        }

        // Scenario #3: syncDelete return all newly created record since a specific
        // synced date
        res = generalSyncService.pullDeletion(oBetweenStr, userId, 0, 50).getContent();
        assertTrue(res.size() == 1);

        // Scenario #4: syncDelete return paginated record, pagination is correct
        res = generalSyncService.pullDeletion(oPastStr, userId, 0, 1).getContent();
        assertTrue(res.size() == 1);
        res = generalSyncService.pullDeletion(oPastStr, userId, 1, 1).getContent();
        assertTrue(res.size() == 1);
        // ------------------------------------------------------------------------------------------------

        // ------------------------------------------------------------------------------------------------
        // Scnario #1:
    }

    @Test
    @Order(5)
    void userService_ShouldReturnAddedUser() {
        String email = "servicetest_testuseradded@gmail.com";
        UserDto user = createUser(email, "servicetest_testuseradded");

        UserDto user2 = userService.getUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found"));

        UserDto user3 = userService.getUserById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Entity not found"));

        assertTrue(user.getId().equals(user2.getId()));
        assertTrue(user.getEmail().equals(user2.getEmail()));
        assertTrue(user.getUsername().equals(user2.getUsername()));

        assertTrue(user.getId().equals(user3.getId()));
        assertTrue(user.getEmail().equals(user3.getEmail()));
        assertTrue(user.getUsername().equals(user3.getUsername()));
    }
}
