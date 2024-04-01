package com.memori.memori_api.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserPatchRequest {
    @Email
    private String email;

    private String username;

    private Boolean isEmailVerified;

    private Long setStorageSizeInByte;

    // private String tier;

    @Min(value = 0, message = "Daily reset time must be at least 0")
    @Max(value = 23, message = "Daily reset timemust not exceed 100")
    private Integer dailyResetTime;

    private String timezone;
}
