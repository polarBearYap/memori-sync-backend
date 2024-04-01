package com.memori.memori_firebase.services;

import java.security.interfaces.RSAPublicKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.memori.memori_firebase.configurations.FirebaseConfig;

@Service
public class FirebaseAppCheckVerifier {

    private static final Logger log = LoggerFactory.getLogger(FirebaseAppCheckVerifier.class);

    private final FirebaseConfig firebaseConfig;
    private final JwkProvider jwkProvider;

    public FirebaseAppCheckVerifier(FirebaseConfig firebaseConfig,
            JwkProvider jwkProvider) {
        this.firebaseConfig = firebaseConfig;
        // # 1. Obtain the Firebase App Check Public Keys
        // # Note: It is not recommended to hard code these keys as they rotate,
        // # but you should cache them for up to 6 hours.
        this.jwkProvider = jwkProvider;
    }

    private String joinUrl(String left, String right) {
        // Remove excessive "/" and always add "/" in between
        String result = (left.endsWith("/") ? left : left + "/") + (right.startsWith("/") ? right.substring(1) : right);
        return result;
    }

    public boolean verifyToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            Jwk jwk = jwkProvider.get(jwt.getKeyId());

            // # 3. Ensure the token's header uses the algorithm RS256/Check if the
            // algorithm matches RS256
            if (!"RS256".equals(jwt.getAlgorithm())) {
                return false;
            }

            // # 4. Ensure the token's header has type JWT/token type is JWT
            if (!"JWT".equals(jwt.getType())) {
                return false;
            }

            // # 2. Verify the signature on the App Check token
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
            JWTVerifier verifier = JWT.require(algorithm)
                    // # 5. Ensure the token is issued by App Check
                    .withIssuer(joinUrl(firebaseConfig.getAppCheck().getIssuerEndpoint(), firebaseConfig.getProjectNumber()))
                    // # 7. Ensure the token's audience matches your project
                    .withAudience(
                            joinUrl(firebaseConfig.getAppCheck().getAudienceEndpoint(), firebaseConfig.getProjectNumber()))
                    // # 6. Ensure the token is not expired
                    .acceptExpiresAt(0)
                    .build();

            jwt = verifier.verify(token);

            // # 8. The token's subject will be the app ID, you may optionally filter
            // against
            // # an allow list
            // Optionally, check the token's subject (app ID) against an allow list
            // String subject = jwt.getSubject();
            // Implement your logic here, for example:
            // if (!allowedAppIds.contains(subject)) {
            // return false;
            // }

            // All checks passed, token is valid
            return true;
        } catch (AlgorithmMismatchException e) {
            return false;
        } catch (InvalidClaimException e) {
            return false;
        } catch (JWTDecodeException e) {
            return false;
        } catch (SignatureVerificationException e) {
            return false;
        } catch (JWTVerificationException e) {
            return false;
        } catch (Exception e) {
            log.error("Error verifying App Check token: ", e);
            return false;
        }
    }
}
