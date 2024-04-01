package com.memori.memori_api.requests;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PullSpecificRequest {

    @NotBlank
    private List<String> entityIds;

    @NotBlank
    private String userId;
}
