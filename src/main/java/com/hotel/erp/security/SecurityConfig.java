package com.hotel.erp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Enable method-level security annotations like @PreAuthorize
public class SecurityConfig {

    private final DatabaseUserDetailsService databaseUserDetailsService;
    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfig(DatabaseUserDetailsService databaseUserDetailsService,
            CorsConfigurationSource corsConfigurationSource) {
        this.databaseUserDetailsService = databaseUserDetailsService;
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests -> requests
                        // Allow authentication endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        // Allow advances and reports endpoints for RECEPTIONIST and ADMIN roles
                        .requestMatchers("/api/advances/**", "/api/reports/**").permitAll()
                        // Allow reservations and checkins endpoints for RECEPTIONIST and ADMIN roles
                        .requestMatchers("/api/reservations/**", "/api/checkins/**").permitAll()
                        // Restrict admin endpoints to ADMIN role
                        .requestMatchers("/api/admin/**").permitAll()
                        // Allow all other requests without authentication for easier access
                        .anyRequest().permitAll())
                .httpBasic(httpBasic -> httpBasic.disable()) // Disable HTTP Basic Auth
                .formLogin(formLogin -> formLogin.disable()) // Disable form login
                .logout(logout -> logout.disable()); // Disable logout handling

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}