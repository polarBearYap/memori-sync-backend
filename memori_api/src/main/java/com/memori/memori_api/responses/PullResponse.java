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
public class PullResponse {
    @JsonProperty("message")
    @Builder.Default
    private String message = "Not specified";

    @JsonProperty("totalPages")
    private Integer totalPages;

    @JsonProperty("totalElements")
    private Long totalElements;

    @JsonProperty("currentPageNumber")
    private Integer currentPageNumber;

    @JsonProperty("hasNextPage")
    private Boolean hasNextPage;

    @JsonProperty("hasPreviousPage")
    private Boolean hasPreviousPage;

    @JsonProperty("items")
    @Builder.Default
    private List<SyncEntityDto> items = new ArrayList<>();
}