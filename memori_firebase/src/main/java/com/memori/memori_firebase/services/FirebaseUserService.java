package com.memori.memori_firebase.services;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.google.firebase.auth.AuthErrorCode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;

@Service
@Profile("test")
public class FirebaseUserService {

    private final FirebaseAuth firebaseAuth;

    public FirebaseUserService(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public String createUser(String email, String password) {
        UserRecord userRecord = null;
        try {
            userRecord = firebaseAuth.getUserByEmail(email);
        } catch (FirebaseAuthException e) {
            if (e.getAuthErrorCode() == AuthErrorCode.USER_NOT_FOUND) {
                CreateRequest request = new CreateRequest()
                        .setEmail(email)
                        .setPassword(password)
                        .setEmailVerified(true)
                        .setDisabled(false);
                try {
                    userRecord = firebaseAuth.createUser(request);
                } catch (FirebaseAuthException e1) {
                    return "";
                }
            }
        }
        return userRecord == null ? "" : userRecord.getUid();
    }
    
    public String generateCustomToken(String uId) {
        String customToken;
        try {
            customToken = firebaseAuth.createCustomToken(uId);
        } catch (FirebaseAuthException e) {
            return "";
        }
        return customToken;
    }
}
