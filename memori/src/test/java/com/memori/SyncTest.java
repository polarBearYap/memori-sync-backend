package com.memori;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.memori.memori_api.responses.PullResponse;
import com.memori.memori_api.responses.UploadResponse;
import com.memori.memori_service.dtos.DeckSettingsDto;
import com.memori.memori_service.dtos.PullSyncResponseDto;
import com.memori.memori_service.dtos.StudyOptionDto;
import com.memori.memori_service.dtos.SyncEntityDto;
import com.memori.memori_service.dtos.SyncEntityDto.Action;
import com.memori.memori_service.dtos.UserDto;
import com.memori.memori_service.services.GeneralSyncService;

public class SyncTest extends BaseTest {

    @Autowired
    GeneralSyncService generalSyncService;

    @Autowired
    private MockMvc mockMvc;

    private String token;

    @Test
    void sync_ShouldReturn400_ForInvalidInput() throws Exception {
        final String email = "sync_test1@example.com";
        final String password = "sync_test1";
        UserDto userDto = createUser(email, password);
        token = getAccessToken(email, password);

        OffsetDateTime oNow = OffsetDateTime.now(ZoneOffset.UTC);
        String oNowStr = oNow.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        String userId = userDto.getId();

        String pageNumber = "0";
        String pageSize = "50";

        // Test status code 400
        // Scenario 1: Invalid synced date
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(pullCreatePath)
                .param("lastSyncDateTimeStr", "Invalid sync date")
                .param("userId", userId)
                .param("pageNumber", pageNumber)
                .param("pageSize", pageSize)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));
        resultActions.andExpect(status().isBadRequest());

        // Scenario 2: Invalid user id
        resultActions = mockMvc.perform(MockMvcRequestBuilders.get(pullCreatePath)
                .param("lastSyncDateTimeStr", oNowStr)
                // .param("userId", userId)
                .param("pageNumber", pageNumber)
                .param("pageSize", pageSize)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));
        resultActions.andExpect(status().isBadRequest());

        // Scenario 3: Invalid page number (negative value)
        resultActions = mockMvc.perform(MockMvcRequestBuilders.get(pullCreatePath)
                .param("lastSyncDateTimeStr", oNowStr)
                .param("userId", userId)
                .param("pageNumber", "-5")
                .param("pageSize", "-50")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void syncEntityDtoDeserializaton_ShouldCorrect() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        SyncEntityDto dsTest = objectMapper.readValue(readJson("sync/deck_settings_response.json"),
                SyncEntityDto.class);
        assertTrue(dsTest instanceof DeckSettingsDto);

        SyncEntityDto soTest = objectMapper.readValue(readJson("sync/study_option_response.json"), SyncEntityDto.class);
        assertTrue(soTest instanceof StudyOptionDto);

        List<SyncEntityDto> seTest = objectMapper.readValue(readJson("sync/sync_entity_list_response.json"),
                new TypeReference<List<SyncEntityDto>>() {
                });
        assertTrue(seTest.size() == 2);
        assertTrue(seTest.get(0) instanceof StudyOptionDto);
        assertTrue(seTest.get(1) instanceof DeckSettingsDto);

        PullResponse urTest = objectMapper.readValue(readJson("sync/upload_response.json"), PullResponse.class);
        ;
        assertTrue(urTest.getItems().size() == 2);
        assertTrue(urTest.getItems().get(0) instanceof StudyOptionDto);
        assertTrue(urTest.getItems().get(1) instanceof DeckSettingsDto);
    }

    @Test
    void syncCreation_ShouldReturn200_ForValidInput() throws Exception {
        final String email = "sync_test2@example.com";
        final String password = "sync_test2";
        UserDto userDto = createUser(email, password);
        token = getAccessToken(email, password);

        OffsetDateTime oPast = OffsetDateTime.now(ZoneOffset.UTC);
        String oPastStr = oPast.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        OffsetDateTime oNow = oPast.plusMinutes(5);
        String oNowStr = oNow.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        String userId = userDto.getId();

        String pageNumber = "0";
        String pageSize = "50";

        // Test status code 400
        // Scenario 1: Empty output
        ResultActions resultActions = mockMvc
                .perform(buildPullRequest(pullCreatePath, userId, pageNumber, pageSize, oNowStr, token));
        resultActions.andExpect(status().isOk());

        List<SyncEntityDto> requests = new ArrayList<>();

        DeckSettingsDto ds = createDefaultDeckSettings();
        ds.setId(UUID.randomUUID().toString());
        ds.setAction(Action.CREATE);
        ds.setUserId(userId);
        ds.setCreatedAt(oNowStr);
        ds.setLastModified(oNowStr);
        requests.add(ds);

        StudyOptionDto so = createDefaultStudyOptionDto();
        String studyOptionId = UUID.randomUUID().toString();
        so.setId(studyOptionId);
        so.setAction(Action.CREATE);
        so.setUserId(userId);
        ds.setCreatedAt(oNowStr);
        ds.setLastModified(oNowStr);
        requests.add(so);

        generalSyncService.normalPush(requests);

        // Scenario 2: Non-empty output
        resultActions = mockMvc.perform(buildPullRequest(pullCreatePath, userId, pageNumber, pageSize, oPastStr, token));
        resultActions.andExpect(status().isOk());
        PullResponse pullResponse = deserializePullResponse(resultActions);
        assertTrue(pullResponse.getItems().size() == 2);
        for (SyncEntityDto syncEntityDto : pullResponse.getItems()) {
            if (syncEntityDto.getEntityType().equals("DeckSettings")) {
                assertTrue(syncEntityDto instanceof DeckSettingsDto);
                assertTrue(syncEntityDto.getId().equals(ds.getId()));
            } else {
                assertTrue(syncEntityDto instanceof StudyOptionDto);
                assertTrue(syncEntityDto.getId().equals(so.getId()));
            }
            assertTrue(syncEntityDto.getAction().equals(Action.CREATE));
        }

        // Scenario 1: Empty output for edited entities
        resultActions = mockMvc.perform(buildPullRequest(pullUpdatePath, userId, pageNumber, pageSize, oPastStr, token));
        resultActions.andExpect(status().isOk());
        pullResponse = deserializePullResponse(resultActions);
        assertTrue(pullResponse.getItems().size() == 0);

        // Scenario 1: Empty output for deleted entities
        resultActions = mockMvc.perform(buildPullRequest(pullDeletePath, userId, pageNumber, pageSize, oPastStr, token));
        resultActions.andExpect(status().isOk());
        pullResponse = deserializePullResponse(resultActions);
        assertTrue(pullResponse.getItems().size() == 0);
    }

    @Test
    void syncUpdate_ShouldReturn200_ForValidInput() throws Exception {
        final String email = "sync_test3@example.com";
        final String password = "sync_test3";
        UserDto userDto = createUser(email, password);
        token = getAccessToken(email, password);

        OffsetDateTime oPast = OffsetDateTime.now(ZoneOffset.UTC);
        String oPastStr = oPast.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        OffsetDateTime oNow = oPast.plusMinutes(5);
        String oNowStr = oNow.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        OffsetDateTime oNext = oNow.plusMinutes(5);
        String oNextStr = oNext.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        String userId = userDto.getId();

        String pageNumber = "0";
        String pageSize = "50";

        // Test status code 400
        // Scenario 1: Empty output
        ResultActions resultActions = mockMvc
                .perform(buildPullRequest(pullUpdatePath, userId, pageNumber, pageSize, oNowStr, token));
        resultActions.andExpect(status().isOk());

        List<SyncEntityDto> requests = new ArrayList<>();

        DeckSettingsDto ds = createDefaultDeckSettings();
        ds.setId(UUID.randomUUID().toString());
        ds.setAction(Action.CREATE);
        ds.setUserId(userId);
        ds.setCreatedAt(oPastStr);
        ds.setLastModified(oPastStr);
        requests.add(ds);

        StudyOptionDto so = createDefaultStudyOptionDto();
        String studyOptionId = UUID.randomUUID().toString();
        so.setId(studyOptionId);
        so.setAction(Action.CREATE);
        so.setUserId(userId);
        so.setCreatedAt(oNextStr);
        so.setLastModified(oNextStr);
        requests.add(so);

        PullSyncResponseDto dtos = generalSyncService.normalPush(requests);
        assertTrue(dtos.getConflictedItems().size() == 0);

        ds = getDeckSettingsDto(ds.getId());
        ds.setAction(Action.UPDATE);
        ds.setDesiredRetention(0.99);
        ds.setLastModified(oNextStr);
        requests.clear();
        requests.add(ds);

        dtos = generalSyncService.normalPush(requests);
        assertTrue(dtos.getConflictedItems().size() == 0);

        // Scenario 2: Non-empty output
        resultActions = mockMvc.perform(buildPullRequest(pullUpdatePath, userId, pageNumber, pageSize, oNowStr, token));
        resultActions.andExpect(status().isOk());
        PullResponse pullResponse = deserializePullResponse(resultActions);
        assertTrue(pullResponse.getItems().size() == 1);
        SyncEntityDto dto = pullResponse.getItems().get(0);
        assertTrue(dto.getEntityType().equals("DeckSettings"));
        assertTrue(dto instanceof DeckSettingsDto);
        assertTrue(dto.getId().equals(ds.getId()));
        assertTrue(dto.getAction().equals(Action.UPDATE));

        // Scenario 1: Empty output for deleted entities
        resultActions = mockMvc.perform(buildPullRequest(pullDeletePath, userId, pageNumber, pageSize, oNowStr, token));
        resultActions.andExpect(status().isOk());
        pullResponse = deserializePullResponse(resultActions);
        assertTrue(pullResponse.getItems().size() == 0);
    }

    @Test
    void syncDeletion_ShouldReturn200_ForValidInput() throws Exception {
        final String email = "sync_test4@example.com";
        final String password = "sync_test4";
        UserDto userDto = createUser(email, password);
        token = getAccessToken(email, password);

        OffsetDateTime oPast = OffsetDateTime.now(ZoneOffset.UTC);
        String oPastStr = oPast.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        OffsetDateTime oNow = oPast.plusMinutes(5);
        String oNowStr = oNow.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        OffsetDateTime oNext = oNow.plusMinutes(5);
        String oNextStr = oNext.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        String userId = userDto.getId();

        String pageNumber = "0";
        String pageSize = "50";

        // Test status code 400
        // Scenario 1: Empty output
        ResultActions resultActions = mockMvc
                .perform(buildPullRequest(pullDeletePath, userId, pageNumber, pageSize, oNowStr, token));
        resultActions.andExpect(status().isOk());

        List<SyncEntityDto> requests = new ArrayList<>();

        DeckSettingsDto ds = createDefaultDeckSettings();
        ds.setId(UUID.randomUUID().toString());
        ds.setAction(Action.CREATE);
        ds.setUserId(userId);
        ds.setCreatedAt(oPastStr);
        ds.setLastModified(oPastStr);
        requests.add(ds);

        StudyOptionDto so = createDefaultStudyOptionDto();
        String studyOptionId = UUID.randomUUID().toString();
        so.setId(studyOptionId);
        so.setAction(Action.CREATE);
        so.setUserId(userId);
        so.setCreatedAt(oNextStr);
        so.setLastModified(oNextStr);
        requests.add(so);

        PullSyncResponseDto dtos = generalSyncService.normalPush(requests);
        assertTrue(dtos.getConflictedItems().size() == 0);

        ds = getDeckSettingsDto(ds.getId());
        ds.setAction(Action.DELETE);
        ds.setDesiredRetention(0.99);
        ds.setLastModified(oNextStr);
        ds.setDeletedAt(oNextStr);
        requests.clear();
        requests.add(ds);

        dtos = generalSyncService.normalPush(requests);
        assertTrue(dtos.getConflictedItems().size() == 0);

        // Scenario 2: Non-empty output
        resultActions = mockMvc.perform(buildPullRequest(pullDeletePath, userId, pageNumber, pageSize, oNowStr, token));
        resultActions.andExpect(status().isOk());
        PullResponse pullResponse = deserializePullResponse(resultActions);
        assertTrue(pullResponse.getItems().size() == 1);
        SyncEntityDto dto = pullResponse.getItems().get(0);
        assertTrue(dto.getEntityType().equals("DeckSettings"));
        assertTrue(dto instanceof DeckSettingsDto);
        assertTrue(dto.getId().equals(ds.getId()));
        assertTrue(dto.getAction().equals(Action.DELETE));

        // Scenario 1: Empty output for deleted entities
        resultActions = mockMvc.perform(buildPullRequest(pullUpdatePath, userId, pageNumber, pageSize, oNowStr, token));
        resultActions.andExpect(status().isOk());
        pullResponse = deserializePullResponse(resultActions);
        assertTrue(pullResponse.getItems().size() == 0);
    }

    private MockHttpServletRequestBuilder buildUploadRequest(String apiPath, String filename) throws IOException {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(apiPath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(readJson(filename))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .with(csrf());
        return requestBuilder;
    }

    @Test
    void syncPush_ShouldReturn200_ForValidInput() throws Exception {
        String dsId = "c4a9b705-f657-4a89-bcc7-760e5be60a69";
        String soId = "25fe80f1-6a48-4333-b4e5-4b19296967ff";

        String createNoConflict = "sync/upload_request_creation_no_conflict.json";
        String createWithConfict = "sync/upload_request_creation_with_conflict.json";

        String updateNoConflict = "sync/upload_request_update_no_conflict.json";
        String updateWithConfict = "sync/upload_request_update_with_conflict.json";

        String deleteNoConflict = "sync/upload_request_deletion_no_conflict.json";
        String deleteWithConfict = "sync/upload_request_deletion_with_conflict.json";

        createUserWithId("3e4d57ff-4b87-4863-8c75-a05932c06ba2", "sync_push_test@gmail.com", "sync_push_test");

        // Scenario 1: Sync normal push return empty output (Indicate there is no
        // conflict)
        // 1.1: Creation
        ResultActions resultActions = mockMvc
                .perform(buildUploadRequest(pushNormalPath, createNoConflict));
        resultActions.andExpect(status().isOk());
        UploadResponse uploadResponse = deserializeUploadResponse(resultActions);
        assertTrue(uploadResponse.getConflictedItems().size() == 0);

        // Scenario 2: Sync normal push return non-empty output (Indicate there is
        // conflict)
        // 2.1: Creation
        resultActions = mockMvc
                .perform(buildUploadRequest(pushNormalPath, createWithConfict));
        resultActions.andExpect(status().isOk());
        uploadResponse = deserializeUploadResponse(resultActions);
        assertTrue(uploadResponse.getConflictedItems().size() == 1);
        assertTrue(uploadResponse.getConflictedItems().get(0).getId().equals(soId));

        // Scneario 3: Sync override push return empty output (Override conflict)
        // 3.1: Creation
        resultActions = mockMvc
                .perform(buildUploadRequest(pushOverridePath, createWithConfict));
        resultActions.andExpect(status().isOk());
        uploadResponse = deserializeUploadResponse(resultActions);
        assertTrue(uploadResponse.getConflictedItems().size() == 0);

        StudyOptionDto so = getStudyOption(soId);
        assertTrue(so.getVersion().equals(1L));
        assertTrue(so.getMode().equals(2));
        assertTrue(so.getSortOption().equals(2));

        // 1.2: Update
        resultActions = mockMvc
                .perform(buildUploadRequest(pushNormalPath, updateNoConflict));
        resultActions.andExpect(status().isOk());
        uploadResponse = deserializeUploadResponse(resultActions);
        assertTrue(uploadResponse.getConflictedItems().size() == 0);

        // 2.2: Update
        resultActions = mockMvc
                .perform(buildUploadRequest(pushNormalPath, updateWithConfict));
        resultActions.andExpect(status().isOk());
        uploadResponse = deserializeUploadResponse(resultActions);
        assertTrue(uploadResponse.getConflictedItems().size() == 1);
        assertTrue(uploadResponse.getConflictedItems().get(0).getId().equals(dsId));

        // 3.2: Update
        resultActions = mockMvc
                .perform(buildUploadRequest(pushOverridePath, updateWithConfict));
        resultActions.andExpect(status().isOk());
        uploadResponse = deserializeUploadResponse(resultActions);
        assertTrue(uploadResponse.getConflictedItems().size() == 0);

        DeckSettingsDto ds = getDeckSettingsDto(dsId);
        assertTrue(ds.getVersion().equals(2L));
        assertTrue(ds.getDesiredRetention().equals(0.99));
        assertTrue(ds.getInterdayPriority().equals(1));

        // 1.3: Deletion
        resultActions = mockMvc
                .perform(buildUploadRequest(pushNormalPath, deleteNoConflict));
        resultActions.andExpect(status().isOk());
        uploadResponse = deserializeUploadResponse(resultActions);
        assertTrue(uploadResponse.getConflictedItems().size() == 0);
        so = getStudyOption(soId);
        assertTrue(so.getVersion().equals(2L));
        assertTrue(so.getDeletedAt() != null);

        // 2.3: Deletion
        resultActions = mockMvc
                .perform(buildUploadRequest(pushNormalPath, deleteWithConfict));
        resultActions.andExpect(status().isOk());
        uploadResponse = deserializeUploadResponse(resultActions);
        assertTrue(uploadResponse.getConflictedItems().size() == 1);
        assertTrue(uploadResponse.getConflictedItems().get(0).getId().equals(soId));

        // 3.3: Deletion
        resultActions = mockMvc
                .perform(buildUploadRequest(pushOverridePath, deleteWithConfict));
        resultActions.andExpect(status().isOk());
        uploadResponse = deserializeUploadResponse(resultActions);
        assertTrue(uploadResponse.getConflictedItems().size() == 0);

        so = getStudyOption(soId);
        assertTrue(so.getVersion().equals(3L));
    }
}
