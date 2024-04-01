package com.memori.memori_api.requests;

import com.memori.memori_api.validators.ValidIsoDateTime;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PullRequest {
    
    @NotBlank
    @ValidIsoDateTime
    private String lastSyncDateTimeStr;
    
    @NotBlank
    private String userId;
    
    @Min(value = 0, message = "Page number must be at least 0")
    private int pageNumber;
    
    @Min(value = 1, message = "Page size must be at least 1")
    @Max(value = 100, message = "Page size must not exceed 100")
    private int pageSize;
}