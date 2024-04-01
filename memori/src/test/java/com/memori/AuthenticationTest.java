package com.memori;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

class AuthenticationTest extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void heartbeatEndpoint_ShouldReturnUnauthorized_ForInvalidToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/heartbeat"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void heartbeatEndpoint_ShouldReturnUnauthorized_ForValidToken() throws Exception {
        String email = "testuser2@example.com";
        String password = "password456";
        String token = getAccessToken(email, password);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/heartbeat")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
