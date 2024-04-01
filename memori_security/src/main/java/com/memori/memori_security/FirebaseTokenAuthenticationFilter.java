package com.memori.memori_security;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.memori.memori_firebase.configurations.FirebaseConfig;
import com.memori.memori_firebase.models.FirebaseVerificationResponse;
import com.memori.memori_firebase.services.FirebaseAppCheckVerifier;
import com.memori.memori_firebase.services.FirebaseUserTokenVerifier;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FirebaseTokenAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(FirebaseTokenAuthenticationFilter.class);
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String APP_CHECK_HEADER = "X-Firebase-AppCheck";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String UID_ATTRIBUTE = "uid";

    private final FirebaseConfig firebaseConfig;
    private final FirebaseAppCheckVerifier appCheckVerifier;
    private final FirebaseUserTokenVerifier userTokenVerifier;

    public FirebaseTokenAuthenticationFilter(FirebaseConfig firebaseConfig, FirebaseUserTokenVerifier userTokenVerifier,
            FirebaseAppCheckVerifier appCheckVerifier) {
        this.firebaseConfig = firebaseConfig;
        this.appCheckVerifier = appCheckVerifier;
        this.userTokenVerifier = userTokenVerifier;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

            if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
                String idToken = extractToken(authorizationHeader);
                FirebaseVerificationResponse resp = userTokenVerifier.verifyToken(idToken, true);

                if (resp.getIsAuthenticated() &&
                        (!firebaseConfig.getAppCheck().getEnable() ||
                                isAppCheckTokenValid(request))) {
                    request.setAttribute(UID_ATTRIBUTE, resp.getUid());
                    filterChain.doFilter(request, response);
                    return;
                }
            }
        } catch (Exception e) {
            log.error("Error verifying Firebase token: ", e);
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    private boolean isAppCheckTokenValid(HttpServletRequest request) {
        String appCheckHeader = request.getHeader(APP_CHECK_HEADER);
        if (appCheckHeader == null || appCheckHeader.isBlank())
            return false;
        return appCheckVerifier.verifyToken(appCheckHeader);
    }

    private String extractToken(String authorizationHeader) {
        return authorizationHeader.substring(BEARER_PREFIX.length());
    }

}
