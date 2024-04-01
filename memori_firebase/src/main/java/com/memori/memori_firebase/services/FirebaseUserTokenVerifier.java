package com.memori.memori_firebase.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.firebase.auth.AuthErrorCode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.memori.memori_firebase.models.FirebaseVerificationResponse;

@Service
public class FirebaseUserTokenVerifier {

    private static final Logger log = LoggerFactory.getLogger(FirebaseUserTokenVerifier.class);

    private final FirebaseAuth firebaseAuth;

    public FirebaseUserTokenVerifier(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public FirebaseVerificationResponse verifyToken(String idToken, Boolean checkRevoked) throws Exception {
        var builder = FirebaseVerificationResponse.builder();
        try {
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(idToken, checkRevoked);
            String uid = decodedToken.getUid();
            return builder
                    .isAuthenticated(true)
                    .uid(uid)
                    .build();
        } catch (FirebaseAuthException e) {
            if (e.getAuthErrorCode() == AuthErrorCode.EXPIRED_ID_TOKEN)
                return builder
                        .isTokenExpired(true)
                        .build();
            else if (e.getAuthErrorCode() == AuthErrorCode.INVALID_ID_TOKEN)
                return builder
                        .isTokenInvalid(true)
                        .build();
            else if (e.getAuthErrorCode() == AuthErrorCode.REVOKED_ID_TOKEN)
                return builder
                        .isTokenRevoked(true)
                        .build();
            else if (e.getAuthErrorCode() == AuthErrorCode.USER_DISABLED)
                return builder
                        .isUserDisabled(true)
                        .build();
            // Default case
            return builder.build();
        } catch (Exception e) {
            log.error("Error verifying Firebase token: ", e);
            return builder.build();
        }
    }

}
