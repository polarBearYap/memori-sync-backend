package com.memori.memori_api.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCreationRequest {
    @NotBlank
    @Email
    @JsonProperty("email")
    private String email;

    @NotBlank
    @JsonProperty("username")
    private String username;

    @NotNull
    @JsonProperty("isEmailVerified")
    private Boolean isEmailVerified;
}
