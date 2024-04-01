package com.memori;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.memori.memori_api.responses.PullResponse;
import com.memori.memori_api.responses.UploadResponse;
import com.memori.memori_domain.Card.State;
import com.memori.memori_firebase.services.FirebaseUserService;
import com.memori.memori_service.dtos.CardDto;
import com.memori.memori_service.dtos.CardTagDto;
import com.memori.memori_service.dtos.DeckDto;
import com.memori.memori_service.dtos.DeckSettingsDto;
import com.memori.memori_service.dtos.StudyOptionDto;
import com.memori.memori_service.dtos.UserDto;
import com.memori.memori_service.mappers.DeckSettingsMapper;
import com.memori.memori_service.mappers.StudyOptionMapper;
import com.memori.memori_service.services.DeckSettingsService;
import com.memori.memori_service.services.StudyOptionService;
import com.memori.memori_service.services.UserService;

import jakarta.persistence.EntityNotFoundException;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles({ "h2", "test" })
public class BaseTest {

    protected final String parentSyncPath = "/api/schedulecard";

    protected final String pullCreatePath = parentSyncPath + "/pullcreate";

    protected final String pullUpdatePath = parentSyncPath + "/pullupdate";

    protected final String pullDeletePath = parentSyncPath + "/pulldelete";

    protected final String pushNormalPath = parentSyncPath + "/upload";

    protected final String pushOverridePath = parentSyncPath + "/forceupload";

    @Autowired
    private FirebaseUserService firebaseUserService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    UserService userService;

    @Autowired
    DeckSettingsMapper deckSettingsMapper;

    @Autowired
    DeckSettingsService deckSettingsService;

    @Autowired
    StudyOptionMapper studyOptionMapper;

    @Autowired
    StudyOptionService studyOptionService;

    protected final Map<String, String> jsonContentCache = new HashMap<>();

    protected UserDto createUser(String email, String username) {
        return createUserWithId(UUID.randomUUID().toString(), email, username);
    }

    protected UserDto createUserWithId(String id, String email, String username) {
        UserDto u = new UserDto();
        u.setId(id);
        u.setEmail(email);
        u.setUsername(username);
        u.setIsEmailVerified(true);
        u.setTier(1);
        u.setStorageSizeInByte(0L);
        u.setTimezone("Asia/Singapore");
        u.setDailyResetTime(2);

        assertDoesNotThrow(() -> {
            userService.createUser(u);
        }, "No error should be thrown");
        return u;
    }

    protected String getAccessToken(String email, String password) throws IOException, InterruptedException {
        firebaseUserService.createUser(email, password);
        String emulatorHost = System.getenv("FIREBASE_AUTH_EMULATOR_HOST");
        String clientPath = System.getenv("NODEJS_CLIENT_REL_PATH");
        String credentialsPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
        String jsFilePath = getRelativeFilePath(clientPath).getPath();
        String nodeCommand = "node";
        String httpExt = "http://";
        ProcessBuilder processBuilder = new ProcessBuilder(nodeCommand, jsFilePath, email, password, credentialsPath,
                emulatorHost.startsWith(httpExt) ? emulatorHost : httpExt + emulatorHost);
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        reader.readLine();
        String token = reader.readLine();
        process.waitFor();
        if (token == null)
            token = "";
        assertTrue(token != null && !token.isBlank());
        return token;
    }

    protected File getRelativeFilePath(String relativePath) throws IOException {
        File configFile = new File(relativePath);
        if (!configFile.exists())
            throw new IOException("Configuration file not found at " + configFile.getAbsolutePath());
        return configFile;
    }

    protected String readJson(String filename) throws IOException {
        if (jsonContentCache.containsKey(filename))
            return jsonContentCache.get(filename);
        try (InputStream inputStream = resourceLoader.getResource("classpath:" + filename).getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            String jsonContent = builder.toString();
            jsonContentCache.put(filename, jsonContent);
            return jsonContent;
        }
    }

    protected DeckSettingsDto createDefaultDeckSettings() {
        OffsetDateTime oNow = OffsetDateTime.now(ZoneOffset.UTC);
        String oNowStr = oNow.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        DeckSettingsDto ds = new DeckSettingsDto();
        ds.setCreatedAt(oNowStr);
        ds.setLastModified(oNowStr);
        ds.setVersion(1L);
        ds.setModifiedByDeviceId(UUID.randomUUID().toString());
        ds.setIsDefault(true);
        ds.setLearningSteps("1m 10m 1h");
        ds.setRelearningSteps("1m 10m");
        ds.setMaxNewCardsPerDay(10);
        ds.setMaxReviewPerDay(5);
        ds.setMaximumAnswerSeconds(60);
        ds.setDesiredRetention(0.95);
        ds.setNewPriority(1);
        ds.setInterdayPriority(2);
        ds.setReviewPriority(3);
        ds.setSkipNewCard(false);
        ds.setSkipLearningCard(false);
        ds.setSkipReviewCard(false);

        return ds;
    }

    protected StudyOptionDto createDefaultStudyOptionDto() {
        OffsetDateTime oNow = OffsetDateTime.now(ZoneOffset.UTC);
        String oNowStr = oNow.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        StudyOptionDto so = new StudyOptionDto();
        so.setCreatedAt(oNowStr);
        so.setLastModified(oNowStr);
        so.setVersion(1L);
        so.setModifiedByDeviceId(UUID.randomUUID().toString());
        so.setMode(1);
        so.setSortOption(1);

        return so;
    }

    protected StudyOptionDto getStudyOption(String id) {
        return studyOptionMapper
                .entityToDto(studyOptionService.getById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Entity not found exception")));
    }

    protected DeckSettingsDto getDeckSettingsDto(String id) {
        return deckSettingsMapper
                .entityToDto(deckSettingsService.getById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Entity not found exception")));
    }

    protected PullResponse deserializePullResponse(ResultActions resultActions)
    throws UnsupportedEncodingException, JsonMappingException, JsonProcessingException {
        String jsonString = resultActions.andReturn().getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        PullResponse uploadResponse = objectMapper.readValue(jsonString, PullResponse.class);
        return uploadResponse;
    }

    protected UploadResponse deserializeUploadResponse(ResultActions resultActions)
            throws UnsupportedEncodingException, JsonMappingException, JsonProcessingException {
        String jsonString = resultActions.andReturn().getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        UploadResponse uploadResponse = objectMapper.readValue(jsonString, UploadResponse.class);
        return uploadResponse;
    }

    protected MockHttpServletRequestBuilder buildPullRequest(String apiPath, String userId, String pageNumber, String pageSize, String lastSyncDateTimeStr, String token) {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(apiPath)
                .param("userId", userId)
                .param("pageNumber", pageNumber)
                .param("pageSize", pageSize)
                .param("lastSyncDateTimeStr", lastSyncDateTimeStr)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        return requestBuilder;
    }

    protected DeckDto createDefaultDeck() {
        OffsetDateTime oNow = OffsetDateTime.now(ZoneOffset.UTC);
        String oNowStr = oNow.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        DeckDto d = new DeckDto();
        d.setCreatedAt(oNowStr);
        d.setLastModified(oNowStr);
        d.setVersion(1L);
        d.setModifiedByDeviceId(UUID.randomUUID().toString());
        d.setName("Deck example 1");
        d.setDescription("Deck description 1");
        d.setShareExpirationTime(oNowStr);
        d.setLastReviewTime(oNowStr);
        
        return d;
    }


    protected CardTagDto createDefaultCardTag() {
        OffsetDateTime oNow = OffsetDateTime.now(ZoneOffset.UTC);
        String oNowStr = oNow.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        CardTagDto ct = new CardTagDto();
        ct.setCreatedAt(oNowStr);
        ct.setLastModified(oNowStr);
        ct.setVersion(1L);
        ct.setModifiedByDeviceId(UUID.randomUUID().toString());
        ct.setName("Card tag example 1");
        
        return ct;
    }

    protected CardDto createDefaultCard(String deckId) {
        OffsetDateTime oNow = OffsetDateTime.now(ZoneOffset.UTC);
        String oNowStr = oNow.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        CardDto c = new CardDto();
        c.setCreatedAt(oNowStr);
        c.setLastModified(oNowStr);
        c.setVersion(1L);
        c.setModifiedByDeviceId(UUID.randomUUID().toString());
        c.setFront("{}");
        c.setBack("{}");
        c.setExplanation("{}");
        c.setDisplayOrder(0);
        c.setDifficulty(0.0);
        c.setDue(oNowStr);
        c.setActual_due(oNowStr);
        c.setElapsed_days(0);
        c.setLapses(0);
        c.setLast_review(oNowStr);
        c.setReps(0);
        c.setScheduled_days(0);
        c.setStability(0.0);
        c.setState(State.NEW.getValue());
        c.setIsSuspended(false);
        c.setDeckId(deckId);

        return c;
    }

}
