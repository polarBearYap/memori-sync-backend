package com.memori.memori_api.responses;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.memori.memori_service.dtos.SyncEntityDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadResponse {
    @JsonProperty("message")
    @Builder.Default
    private String message = "Not specified";

    @JsonProperty("conflictedItems")
    @Builder.Default
    private List<SyncEntityDto> conflictedItems = new ArrayList<>();

    @JsonProperty("successfulItems")
    @Builder.Default
    private List<SyncEntityDto> successfulItems = new ArrayList<>();
}