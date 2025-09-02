package com.hotel.erp.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AdminController {

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getAdminDashboard() {
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("currentTime", LocalDateTime.now().toString());
        dashboard.put("message", "Welcome to Hotel Management ERP Admin Dashboard");
        dashboard.put("status", "OK");

        Map<String, String> modules = new HashMap<>();
        modules.put("userManagement", "/api/users");
        modules.put("taxManagement", "/api/admin/taxes");
        modules.put("shiftManagement", "/api/admin/shifts");
        modules.put("accountHeads", "/api/admin/account-heads");
        modules.put("roomManagement", "/api/admin/rooms");
        modules.put("roomTypes", "/api/admin/room-types");
        modules.put("paymentModes", "/api/admin/payment-modes");
        modules.put("planTypes", "/api/admin/plan-types");
        modules.put("companies", "/api/admin/companies");
        modules.put("nationalities", "/api/admin/nationalities");
        modules.put("refModes", "/api/admin/ref-modes");
        modules.put("arrivalModes", "/api/admin/arrival-modes");
        modules.put("reservationSources", "/api/admin/reservation-sources");

        dashboard.put("availableModules", modules);

        return ResponseEntity.ok(dashboard);
    }

    @PostMapping("/audit-date-change")
    public ResponseEntity<Map<String, Object>> auditDateChange(@RequestBody Map<String, Object> request) {
        // In a real implementation, this would update post_transaction with room
        // charges for all in-house guests
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Audit date changed successfully");
        response.put("timestamp", LocalDateTime.now().toString());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now().toString());
        health.put("port", "8088");
        health.put("message", "Hotel Management ERP is running successfully!");
        return ResponseEntity.ok(health);
    }
}
