package com.memori.memori_firebase;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.memori.memori_firebase.models.FirebaseVerificationResponse;
import com.memori.memori_firebase.services.FirebaseUserService;
import com.memori.memori_firebase.services.FirebaseUserTokenVerifier;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FirebaseUserTokenVerifierTest {

    private FirebaseAuth firebaseAuth;
    private FirebaseUserTokenVerifier firebaseUserTokenVerifier;

    private String emulatorHost;
    private String credentialsPath;
    private String clientPath;

    @BeforeAll
    void setUp() throws IOException {
        credentialsPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
        assertTrue(credentialsPath != null && !credentialsPath.isEmpty() && !credentialsPath.isBlank());
        emulatorHost = System.getenv("FIREBASE_AUTH_EMULATOR_HOST");
        String projectId = System.getenv("FIREBASE_PROJECT_ID");
        clientPath = System.getenv("NODEJS_CLIENT_REL_PATH");

        File credentialFile = getRelativeFilePath(credentialsPath);
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new FileInputStream(credentialFile))
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .setProjectId(projectId)
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUserTokenVerifier = new FirebaseUserTokenVerifier(firebaseAuth);
    }

    private File getRelativeFilePath(String relativePath) throws IOException {
        File configFile = new File(relativePath);
        if (!configFile.exists())
            throw new IOException("Configuration file not found at " + configFile.getAbsolutePath());
        return configFile;
    }

    private String getIdTokenFromClient(String customToken) {
        try {
            // Path to the main.js file
            String jsFilePath = getRelativeFilePath(clientPath).getPath();
            String credentialFilePath = credentialsPath;

            // Command to run Node.js with your JavaScript file
            String nodeCommand = "node";
            String httpExt = "http://";

            // Create ProcessBuilder with node command
            ProcessBuilder processBuilder = new ProcessBuilder(nodeCommand, jsFilePath, customToken, credentialFilePath,
                    emulatorHost.startsWith(httpExt) ? emulatorHost : httpExt + emulatorHost);

            // Start the process
            Process process = processBuilder.start();

            // Capture error output
            // BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            // StringBuilder errorOutput = new StringBuilder();
            // String errorLine;
            // while ((errorLine = errorReader.readLine()) != null) {
            //     errorOutput.append(errorLine).append("\n");
            // }

            // Check and log error output
            // if (errorOutput.length() > 0) {
            //     System.out.println("Error Output: " + errorOutput.toString());
            // }

            // Read output from the process
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            line = reader.readLine();

            // Wait for the process to complete
            process.waitFor();

            return line != null ? line : "";
        } catch (IOException | InterruptedException | IllegalStateException e) {
            e.printStackTrace(); // Handle or log the exception
            return "";
        }
    }

    @Test
    void verifyToken_ShouldReturnTrue_ForValidToken() throws Exception {
        FirebaseUserService firebaseUserService = new FirebaseUserService(firebaseAuth);
        String uid = firebaseUserService.createUser("testuser@example.com", "password123");
        String customToken = firebaseUserService.generateCustomToken(uid);
        String testToken = getIdTokenFromClient(customToken);
        FirebaseVerificationResponse resp = firebaseUserTokenVerifier.verifyToken(testToken, true);
        assertTrue(resp.getIsAuthenticated());
    }

    @Test
    void verifyToken_ShouldReturnTrue_ForInvalidToken() throws Exception {
        FirebaseVerificationResponse resp = firebaseUserTokenVerifier.verifyToken("invalid_token", true);
        assertFalse(resp.getIsAuthenticated());
    }
}
