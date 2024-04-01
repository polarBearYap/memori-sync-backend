package com.memori;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.memori.memori_service.dtos.UserDto;
import com.memori.memori_service.services.UserService;

public class UserTest extends BaseTest {
    @Autowired
    UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private String token;

    private final String path = "/api/user";

    private final String email = "user_test@gmail.com";

    private final String username = "user_test";

    private MockHttpServletRequestBuilder buildCreateUserRequest(String jsonBody) {
        return MockMvcRequestBuilders
                .post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .with(csrf());
    }

    private MockHttpServletRequestBuilder buildPatchUserRequest(String jsonBody) {
        return MockMvcRequestBuilders
                .patch(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .with(csrf());
    }

    @Test
    void userManagmenet_ShouldValid() throws Exception {
        token = getAccessToken(email, username);

        // Part 1: Create user
        MockHttpServletRequestBuilder requestBuilder = buildCreateUserRequest("""
                {
                    "username": "user_test",
                    "isEmailVerified": false
                }
                """);
        ResultActions resultActions = mockMvc.perform(requestBuilder);
        resultActions.andExpect(status().isBadRequest());

        requestBuilder = buildCreateUserRequest("""
                {
                    "email": "user_test@gmail.com",
                    "username": "user_test",
                    "isEmailVerified": false
                }
                """);
        resultActions = mockMvc.perform(requestBuilder);
        resultActions.andExpect(status().isOk());

        Optional<UserDto> userDto = userService.getUserByEmail("user_test@gmail.com");
        assertTrue(userDto.isPresent());

        // Part 2: Patch user
        requestBuilder = buildPatchUserRequest("""
                {
                    "email": "user_test_changednotvalid",
                    "isEmailVerified": true
                }
                """);
        resultActions = mockMvc.perform(requestBuilder);
        resultActions.andExpect(status().isBadRequest());

        requestBuilder = buildPatchUserRequest("""
                {
                    "email": "user_test_changed@gmail.com",
                    "isEmailVerified": true
                }
                """);
        resultActions = mockMvc.perform(requestBuilder);
        resultActions.andExpect(status().isOk());

        userDto = userService.getUserByEmail("user_test_changed@gmail.com");
        assertTrue(userDto.isPresent());
    }
}
