package com.memori.memori_firebase.models;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;

@Value
@Builder
public class FirebaseVerificationResponse {
    @Default
    Boolean isAuthenticated = false;
    @Default
    Boolean isTokenRevoked = false;
    @Default
    Boolean isTokenExpired = false;
    @Default
    Boolean isTokenInvalid = false;
    @Default
    Boolean isUserDisabled = false;
    @Default
    String uid = "";
}
