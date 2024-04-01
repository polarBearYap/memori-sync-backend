package com.memori.memori_firebase.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Component
@ConfigurationProperties(prefix = "memori.firebase")
@Data
@Validated
public class FirebaseConfig {
    @NotNull
    private String projectId;

    @NotNull
    private String projectNumber;

    @NotNull
    private String googleApplicationCredentialsPath;

    // private Emulator emulator;

    private AppCheck appCheck;

    // @Data
    // public static class Emulator {
    //     @NotNull
    //     private Boolean enable;

    //     @NotNull
    //     private String host;
    // }

    @Data
    public static class AppCheck {
        @NotNull
        private Boolean enable;

        @NotNull
        private String jwksEndpoint;

        @NotNull
        private String issuerEndpoint;

        @NotNull
        private String audienceEndpoint;
    }
}