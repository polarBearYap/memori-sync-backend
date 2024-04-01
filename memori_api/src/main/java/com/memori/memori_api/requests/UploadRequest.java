package com.memori.memori_api.requests;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.memori.memori_service.dtos.SyncEntityDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UploadRequest {

    @NotNull
    @Valid
    @JsonProperty("items")
    public List<SyncEntityDto> items;
}
