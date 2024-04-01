package com.memori.memori_security;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final FirebaseTokenAuthenticationFilter firebaseTokenAuthenticationFilter;

    public SecurityConfig(FirebaseTokenAuthenticationFilter firebaseTokenAuthenticationFilter) {
        this.firebaseTokenAuthenticationFilter = firebaseTokenAuthenticationFilter;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                // manager.sessionCreationPolicy(STATELESS): It sets the session creation policy to STATELESS. This means that no server-side sessions will be created or maintained for authenticated users. Instead, the application is designed to be stateless, and authentication is typically done using tokens (e.g., JWT) or another stateless mechanism.
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                // addFilterBefore(...): It adds a custom jwtAuthenticationFilter before the standard UsernamePasswordAuthenticationFilter. This custom filter is responsible for processing JWT tokens and performing user authentication based on those tokens.
                .addFilterBefore(
                    firebaseTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}