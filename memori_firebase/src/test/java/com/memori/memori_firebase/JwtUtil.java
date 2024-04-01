package com.memori.memori_firebase;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

public class JwtUtil {

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }

    public static String generateToken(KeyPair keyPair, String issuer, String audience, Date expiredAt, String subject,
            String keyId) {
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        return JWT.create()
                .withIssuer(issuer)
                .withAudience(audience)
                .withExpiresAt(expiredAt) // 1 hour ahead
                .withSubject(subject)
                .withKeyId(keyId)
                .sign(Algorithm.RSA256(publicKey, privateKey));

        // return JWT.create()
        // .withIssuer("https://firebaseappcheck.googleapis.com/mockProjectNumber")
        // .withAudience("projects/mockProjectNumber")
        // .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000)) // 1
        // hour ahead
        // .withSubject("mockSubject")
        // .withKeyId("mockKeyId")
        // .sign(Algorithm.RSA256(publicKey, privateKey));
    }
}
