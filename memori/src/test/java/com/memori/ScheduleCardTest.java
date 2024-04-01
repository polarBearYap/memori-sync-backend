package com.memori;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.hamcrest.core.Is.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import static org.hamcrest.MatcherAssert.assertThat;

public class ScheduleCardTest extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    private final Integer fsrsPort = Integer.valueOf(System.getenv("FSRS_PORT"));
    private final String fsrsPath = System.getenv("FSRS_SCHEDULE_CARD_PATH");

    @RegisterExtension
    WireMockExtension wireMock = WireMockExtension
            .newInstance()
            .options(wireMockConfig().port(fsrsPort))
            .build();

    private String token;

    @BeforeAll
    void setup() throws IOException, InterruptedException {
        final String email = "testuser3@example.com";
        final String password = "password456";
        token = getAccessToken(email, password);
    }

    private URI setupWireMock(String filename) throws Exception {
        if (filename == null || filename.isBlank())
            return null;
        String path = fsrsPath == null ? "" : fsrsPath.startsWith("/") ? fsrsPath : "/" + fsrsPath;
        URI fsrsFullPath = URI.create("http://localhost:" + fsrsPort + path);
        String jsonContent = readJson(filename);
        // Mock Djano python service
        wireMock.stubFor(
                WireMock.post(urlEqualTo(path))
                        .willReturn(aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withStatus(200)
                                .withBody(jsonContent)));
        return fsrsFullPath;
    }

    private RequestBuilder buildRequest(String filename) throws IOException {
        return MockMvcRequestBuilders
                .post("/api/schedulecard")
                .contentType(MediaType.APPLICATION_JSON)
                .content(readJson(filename))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .with(csrf());
    }

    private String getFilePath(String fileName) {
        return "schedule_card/" + fileName;
    }

    @Test
    void wireMockSetup_ShouldCorrect() throws Exception {
        String filename = getFilePath("correct_response.json");

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(setupWireMock(filename))
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> httpResponse = HttpClient.newHttpClient().send(httpRequest,
                HttpResponse.BodyHandlers.ofString());

        assertThat(httpResponse.body(), is(readJson(filename)));
    }

    @Test
    void scheduleCard_ShouldReturn200_ForValidInput() throws Exception {
        String filename = getFilePath("correct_response.json");
        setupWireMock(filename);

        mockMvc.perform(buildRequest(getFilePath("correct_request.json")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json(readJson(filename), true));
    }

    @Test
    void scheduleCard_ShouldReturn400_ForInvalidInput() throws Exception {
        setupWireMock(getFilePath("correct_response.json"));

        mockMvc.perform(buildRequest(getFilePath("incorrect_request_1.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        mockMvc.perform(buildRequest(getFilePath("incorrect_request_2.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void scheduleCard_ShouldReturn500_ForInvalidOutput() throws Exception {
        setupWireMock(getFilePath("incorrect_response_1.json"));

        mockMvc.perform(buildRequest(getFilePath("correct_request.json")))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"message\":\"Fsrs api returns invalid output\",\"data\":null}", true));

        setupWireMock(getFilePath("incorrect_response_2.json"));
        mockMvc.perform(buildRequest(getFilePath("correct_request.json")))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"message\":\"Fsrs api returns invalid output\",\"data\":null}", true));
    }
}
