package com.memori.memori_firebase;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.memori.memori_firebase.configurations.FirebaseConfig;
import com.memori.memori_firebase.services.FirebaseAppCheckVerifier;

@ExtendWith(MockitoExtension.class)
public class FirebaseAppCheckVerifierTest {

    @Mock
    private FirebaseConfig firebaseConfig;

    @Autowired
    private FirebaseAppCheckVerifier firebaseAppCheckVerifier;

    private KeyPair keyPair;

    private String mockToken;

    private String expiredToken;

    final String jwksEndpoint = "https://mockedEndpoint";
    
    final String audienceEndpoint = "projects";
    
    final String projectNumber = "mockProjectNumber";
    
    final String subject = "mockedSubject";
    
    final String keyId = "mockKeyId";

    @BeforeEach
    void setUp() throws Exception {
        // when(firebaseAppCheckConfig.getJwksEndpoint()).thenReturn(jwksEndpoint);
        FirebaseConfig.AppCheck mockedAppCheck = Mockito.mock(FirebaseConfig.AppCheck.class);
        when(mockedAppCheck.getIssuerEndpoint()).thenReturn(jwksEndpoint);
        when(mockedAppCheck.getAudienceEndpoint()).thenReturn(audienceEndpoint);
        when(firebaseConfig.getAppCheck()).thenReturn(mockedAppCheck);
        when(firebaseConfig.getProjectNumber()).thenReturn(projectNumber);

        keyPair = JwtUtil.generateKeyPair();
        String token = JwtUtil.generateToken(
                keyPair,
                jwksEndpoint + "/" + projectNumber,
                audienceEndpoint + "/" + projectNumber,
                new Date(System.currentTimeMillis() + 60 * 60 * 1000),
                subject,
                keyId);
        this.mockToken = token;

        String expiredToken = JwtUtil.generateToken(
            keyPair,
            jwksEndpoint + "/" + projectNumber,
            audienceEndpoint + "/" + projectNumber,
            new Date(System.currentTimeMillis() - 60 * 60 * 1000),
            subject,
            keyId);
        this.expiredToken = expiredToken;

        Jwk mockedJwk = Mockito.mock(Jwk.class);
        when(mockedJwk.getPublicKey()).thenReturn((RSAPublicKey) keyPair.getPublic());
        // when(mockedJwk.getAlgorithm()).thenReturn("RS256");
        // when(mockedJwk.getType()).thenReturn("JWT");
        JwkProvider jwkProvider = Mockito.mock(JwkProvider.class);
        when(jwkProvider.get(keyId)).thenReturn(mockedJwk);

        firebaseAppCheckVerifier = new FirebaseAppCheckVerifier(firebaseConfig, jwkProvider);
    }

    @Test
    void verifyToken_ShouldReturnTrue_ForValidToken() {
        boolean result = firebaseAppCheckVerifier.verifyToken(this.mockToken);
        assertTrue(result);
    }

    @Test
    void verifyToken_ShouldReturnFalse_ForInvalidIssuer() {
        when(firebaseConfig.getAppCheck().getIssuerEndpoint()).thenReturn("invalid_issuer");

        boolean result = firebaseAppCheckVerifier.verifyToken(this.mockToken);
        assertFalse(result);
    }

    @Test
    void verifyToken_ShouldReturnFalse_ForInvalidAudience() {
        when(firebaseConfig.getAppCheck().getAudienceEndpoint()).thenReturn("invalid_audience");

        boolean result = firebaseAppCheckVerifier.verifyToken(this.mockToken);
        assertFalse(result);
    }

    @Test
    void verifyToken_ShouldReturnFalse_ForInvalidProjectNumber() {
        when(firebaseConfig.getProjectNumber()).thenReturn("invalid_audience");

        boolean result = firebaseAppCheckVerifier.verifyToken(this.mockToken);
        assertFalse(result);
    }

    @Test
    void verifyToken_ShouldReturnFalse_ForExpiredToken() {
        boolean result = firebaseAppCheckVerifier.verifyToken(this.expiredToken);
        assertFalse(result);
    }
}
 