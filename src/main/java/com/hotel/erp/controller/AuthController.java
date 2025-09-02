package com.hotel.erp.controller;

import com.hotel.erp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            // For now, just return a simple response if credentials are correct
            String username = credentials.get("username");
            String password = credentials.get("password");

            // Simple check - in production, you'd use proper authentication
            if ("admin".equals(username) && "admin".equals(password)) {
                return ResponseEntity.ok(Map.of(
                        "token", "simple-jwt-token-" + username,
                        "username", username,
                        "role", "ADMIN",
                        "userType", "Admin"));
            } else {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Authentication failed"));
        }
    }
}
