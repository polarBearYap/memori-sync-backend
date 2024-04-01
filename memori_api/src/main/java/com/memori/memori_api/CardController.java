package com.memori.memori_api;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.memori.memori_api.configurations.FsrsConfig;
import com.memori.memori_api.requests.UserCardScheduleRequest;
import com.memori.memori_api.responses.UserCardScheduleResponse;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;

@RestController
@RequestMapping(path = "/api/schedulecard", produces = "application/json")
public class CardController {

    private final FsrsConfig fsrsConfig;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    private static final Logger logger = LoggerFactory.getLogger(CardController.class);

    public CardController(FsrsConfig fsrsConfig, ObjectMapper objectMapper, Validator validator) {
        this.fsrsConfig = fsrsConfig;
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    @PostMapping
    public ResponseEntity<UserCardScheduleResponse> scheduleCard(@Valid @RequestBody UserCardScheduleRequest request,
            BindingResult result) {
        if (result.hasErrors())
            return buildResponse(HttpStatus.BAD_REQUEST, "Invalid request: " + result.getAllErrors());

        URI fullUrl = null;
        try {
            String httpUrl = fsrsConfig.getHttpUrl();
            String path = fsrsConfig.getScheduleCardPath();
            fullUrl = new URI(httpUrl == null ? "/" : httpUrl)
                    .resolve(path == null ? "" : path.startsWith("/") ? path : "/" + path)
                    .normalize();
        } catch (URISyntaxException e) {
            logger.error("Fsrs api is not available: ", e);
            logger.info("Fsrs API not available: URL: " + fsrsConfig.getHttpUrl() + " Path: " + fsrsConfig.getScheduleCardPath());
            return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Fsrs api is not available");
        }

        try {
            HttpClient httpClient = HttpClient.newHttpClient();

            String requestBodyJson = objectMapper.writeValueAsString(request);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(fullUrl)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() == HttpStatus.OK.value()) {
                UserCardScheduleResponse response = objectMapper.readValue(httpResponse.body(),
                        UserCardScheduleResponse.class);
                Set<ConstraintViolation<UserCardScheduleResponse>> violations = validator.validate(response);
                if (violations.isEmpty())
                    return ResponseEntity.ok().body(response);
                else {
                    return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Fsrs api returns invalid output");
                }
            } else {
                return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Fsrs api fails unexpectedly");
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Error ocurred when scheduling cards: ", e);
            return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error sending POST request");
        }
    }

    private ResponseEntity<UserCardScheduleResponse> buildResponse(HttpStatus httpStatus, String message) {
        UserCardScheduleResponse resp = UserCardScheduleResponse.builder().message(message).build();
        logger.info("schedulecard API endpoint: Status code: " + httpStatus.value() + " Message: " + message);
        return ResponseEntity.status(httpStatus).body(resp);
    }
}
