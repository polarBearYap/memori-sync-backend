package com.memori.memori_api.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Component
@ConfigurationProperties(prefix = "memori.fsrs")
@Data
@Validated
public class FsrsConfig {
    @NotNull
    private String httpUrl;

    @NotNull
    private String scheduleCardPath;
}