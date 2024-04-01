package com.memori;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.memori.memori_api.requests.UploadRequest;
import com.memori.memori_api.responses.PullResponse;
import com.memori.memori_api.responses.UploadResponse;
import com.memori.memori_domain.Card.State;
import com.memori.memori_domain.ReviewLog.Rating;
import com.memori.memori_service.dtos.CardDto;
import com.memori.memori_service.dtos.CardTagDto;
import com.memori.memori_service.dtos.CardTagMappingDto;
import com.memori.memori_service.dtos.DeckDto;
import com.memori.memori_service.dtos.DeckSettingsDto;
import com.memori.memori_service.dtos.ReviewLogDto;
import com.memori.memori_service.dtos.SyncEntityDto.Action;
import com.memori.memori_service.dtos.UserDto;
import com.memori.memori_service.services.GeneralSyncService;

public class SyncIntegrationTest extends BaseTest {
    /*
    1) SyncEntities, (Done)
    2) CardTags, (Done)
    3) DeckSettings, (Done)
    4) Decks, (Done)
    5) Cards, (Done)
    6) CardTagMappings,
    7) ReviewLogs,
    */

    @Autowired
    GeneralSyncService generalSyncService;

    @Autowired
    private MockMvc mockMvc;

    private String token;

    private CardTagMappingDto createDefaultCardTagMapping(String cardTagId, String cardId) {
        OffsetDateTime oNow = OffsetDateTime.now(ZoneOffset.UTC);
        String oNowStr = oNow.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        CardTagMappingDto cm = new CardTagMappingDto();
        cm.setCreatedAt(oNowStr);
        cm.setLastModified(oNowStr);
        cm.setVersion(1L);
        cm.setModifiedByDeviceId(UUID.randomUUID().toString());
        cm.setCardTagId(cardTagId);
        cm.setCardId(cardId);

        return cm;
    }

    private ReviewLogDto createDefaultReviewLogDto(String cardId) {
        OffsetDateTime oNow = OffsetDateTime.now(ZoneOffset.UTC);
        String oNowStr = oNow.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        ReviewLogDto rl = new ReviewLogDto();
        rl.setCreatedAt(oNowStr);
        rl.setLastModified(oNowStr);
        rl.setVersion(1L);
        rl.setModifiedByDeviceId(UUID.randomUUID().toString());
        rl.setCardId(cardId);
        rl.setElapsedDays(0);
        rl.setRating(Rating.AGAIN.getValue());
        rl.setReview(oNowStr);
        rl.setScheduledDays(0);
        rl.setState(State.NEW.getValue());
        rl.setReviewDurationInMs(10000);
        rl.setLastReview(oNowStr);

        return rl;
    }

    private MockHttpServletRequestBuilder buildUploadRequest(String apiPath, UploadRequest request) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(apiPath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .with(csrf());
        return requestBuilder;
    }

    private void debugPrintUploadRequest(UploadRequest request) throws JsonMappingException, JsonProcessingException, UnsupportedEncodingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String prettyJson = mapper.writeValueAsString(request);
        System.out.println(prettyJson);
    }

    private void debugPrintPullResponse(ResultActions resultActions) throws JsonMappingException, JsonProcessingException, UnsupportedEncodingException {
        String jsonString = resultActions.andReturn().getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        Object json = mapper.readValue(jsonString, PullResponse.class);
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        String prettyJson = writer.writeValueAsString(json);

        System.out.println(prettyJson);
    }

    private void debugPrintUploadResponse(ResultActions resultActions) throws JsonMappingException, JsonProcessingException, UnsupportedEncodingException {
        String jsonString = resultActions.andReturn().getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        Object json = mapper.readValue(jsonString, UploadResponse.class);
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        String prettyJson = writer.writeValueAsString(json);

        System.out.println(prettyJson);
    }

    @Test
    void syncIntegration_ShouldReturn200() throws Exception {
        final String email = "sync_integration_test@example.com";
        final String password = "sync_test";
        UserDto userDto = createUser(email, password);
        token = getAccessToken(email, password);

        OffsetDateTime oPast = OffsetDateTime.now(ZoneOffset.UTC);
        String oPastStr = oPast.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        OffsetDateTime oNow = oPast.plusMinutes(5);
        String oNowStr = oNow.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        String userId = userDto.getId();

        DeckSettingsDto ds = createDefaultDeckSettings();
        String deckSettingId = UUID.randomUUID().toString();
        ds.setId(deckSettingId);
        ds.setUserId(userId);
        ds.setCreatedAt(oNowStr);
        ds.setLastModified(oNowStr);
        ds.setAction(Action.CREATE);
        ds.setEntityType("DeckSettings");

        DeckDto d = createDefaultDeck();
        String deckId = UUID.randomUUID().toString();
        d.setId(deckId);
        d.setUserId(userId);
        d.setCreatedAt(oNowStr);
        d.setLastModified(oNowStr);
        d.setAction(Action.CREATE);
        d.setEntityType("Deck");
        d.setDeckSettingsId(deckSettingId);

        CardTagDto ct = createDefaultCardTag();
        String cardTagId = UUID.randomUUID().toString();
        ct.setId(cardTagId);
        ct.setUserId(userId);
        ct.setCreatedAt(oNowStr);
        ct.setLastModified(oNowStr);
        ct.setAction(Action.CREATE);
        ct.setEntityType("CardTag");

        CardDto c = createDefaultCard(deckId);
        String cardId = UUID.randomUUID().toString();
        c.setId(cardId);
        c.setUserId(userId);
        c.setCreatedAt(oNowStr);
        c.setLastModified(oNowStr);
        c.setAction(Action.CREATE);
        c.setEntityType("Card");

        CardTagMappingDto ctm = createDefaultCardTagMapping(cardTagId, cardId);
        ctm.setId(UUID.randomUUID().toString());
        ctm.setUserId(userId);
        ctm.setCreatedAt(oNowStr);
        ctm.setLastModified(oNowStr);
        ctm.setAction(Action.CREATE);
        ctm.setEntityType("CardTagMapping");

        ReviewLogDto rl = createDefaultReviewLogDto(cardId);
        rl.setId(UUID.randomUUID().toString());
        rl.setUserId(userId);
        rl.setCreatedAt(oNowStr);
        rl.setLastModified(oNowStr);
        rl.setAction(Action.CREATE);
        rl.setEntityType("ReviewLog");

        UploadRequest request = new UploadRequest();
        request.items = new ArrayList<>();

        request.items.add(ds);
        request.items.add(d);
        request.items.add(ct);
        request.items.add(c);
        request.items.add(ctm);
        request.items.add(rl);

        debugPrintUploadRequest(request);

        ResultActions resultActions = mockMvc
                .perform(buildUploadRequest(pushNormalPath, request));
        resultActions.andExpect(status().isOk());
        UploadResponse uploadResponse = deserializeUploadResponse(resultActions);
        assertTrue(uploadResponse.getConflictedItems().size() == 0);

        debugPrintUploadResponse(resultActions);

        String pageNumber = "0";
        String pageSize = "50";
                
        resultActions = mockMvc.perform(buildPullRequest(pullCreatePath, userId, pageNumber, pageSize, oPastStr, token));
        resultActions.andExpect(status().isOk());
        PullResponse pullResponse = deserializePullResponse(resultActions);
        assertTrue(pullResponse.getItems().size() == 6);

        debugPrintPullResponse(resultActions);
    }
}
