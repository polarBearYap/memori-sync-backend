package com.memori.memori_firebase.configurations;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;

@Configuration
public class FirebaseBeanConfig {

    @Autowired
    private FirebaseConfig firebaseConfig;

    @Bean
    FirebaseApp firebaseApp() throws IOException {
        // Optionally set system properties here if needed

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .setProjectId(firebaseConfig.getProjectId())
                .build();

        // Initialize the default app only if not already initialized to avoid
        // exceptions on re-initialization
        if (FirebaseApp.getApps().isEmpty()) { // Make sure it's not initialized more than once
            return FirebaseApp.initializeApp(options);
        } else {
            return FirebaseApp.getInstance(); // Return the already initialized instance
        }
    }

    @Bean
    @DependsOn("firebaseApp")
    FirebaseAuth firebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Bean
    JwkProvider jwkProvider(FirebaseConfig firebaseConfig) throws MalformedURLException {
        // Initialize and return a JwkProvider bean
        return new JwkProviderBuilder(new URL(firebaseConfig.getAppCheck().getJwksEndpoint()))
                .cached(10, 5, TimeUnit.HOURS)
                .rateLimited(10, 1, TimeUnit.MINUTES)
                .build();
    }
}
