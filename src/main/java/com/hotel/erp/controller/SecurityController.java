package com.hotel.erp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * Security Management Controller
 * Handles hotel security, access control, and surveillance management
 */
@RestController
@RequestMapping("/api/security")
@CrossOrigin(origins = "*")
public class SecurityController {

    /**
     * Get all security incidents
     */
    @GetMapping("/incidents")
    public ResponseEntity<Map<String, Object>> getAllIncidents() {
        try {
            List<Map<String, Object>> incidents = Arrays.asList(
                    createIncident(1L, "THEFT", "Minor theft in guest room", "RESOLVED"),
                    createIncident(2L, "UNAUTHORIZED_ACCESS", "Unauthorized access to restricted area",
                            "INVESTIGATING"),
                    createIncident(3L, "SUSPICIOUS_ACTIVITY", "Suspicious person in lobby", "RESOLVED"),
                    createIncident(4L, "SAFETY_VIOLATION", "Safety protocol violation", "PENDING"),
                    createIncident(5L, "EMERGENCY", "Medical emergency in room 205", "RESOLVED"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Security incidents retrieved successfully");
            response.put("data", incidents);
            response.put("total", incidents.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch security incidents: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Create security incident
     */
    @PostMapping("/incidents")
    public ResponseEntity<Map<String, Object>> createIncident(@RequestBody Map<String, Object> incidentData) {
        try {
            Long newId = System.currentTimeMillis();
            Map<String, Object> createdIncident = new HashMap<>(incidentData);
            createdIncident.put("id", newId);
            createdIncident.put("status", "PENDING");
            createdIncident.put("reportedDate", LocalDateTime.now().toString());
            createdIncident.put("incidentNumber", "INC_" + newId);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Security incident created successfully");
            response.put("data", createdIncident);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to create security incident: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get access control logs
     */
    @GetMapping("/access-logs")
    public ResponseEntity<Map<String, Object>> getAccessLogs(@RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            List<Map<String, Object>> logs = Arrays.asList(
                    createAccessLog(1L, "John Doe", "EMP001", "Main Entrance", "GRANTED"),
                    createAccessLog(2L, "Jane Smith", "EMP002", "Server Room", "DENIED"),
                    createAccessLog(3L, "Admin User", "ADMIN", "Management Office", "GRANTED"),
                    createAccessLog(4L, "Security Guard", "SEC001", "All Areas", "GRANTED"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Access logs retrieved successfully");
            response.put("data", logs);
            response.put("dateRange", Map.of("start", startDate, "end", endDate));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch access logs: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get surveillance camera status
     */
    @GetMapping("/cameras")
    public ResponseEntity<Map<String, Object>> getCameraStatus() {
        try {
            List<Map<String, Object>> cameras = Arrays.asList(
                    createCamera(1L, "CAM001", "Main Lobby", "ONLINE"),
                    createCamera(2L, "CAM002", "Reception Area", "ONLINE"),
                    createCamera(3L, "CAM003", "Parking Area", "OFFLINE"),
                    createCamera(4L, "CAM004", "Restaurant", "ONLINE"),
                    createCamera(5L, "CAM005", "Emergency Exit", "MAINTENANCE"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Camera status retrieved successfully");
            response.put("data", cameras);
            response.put("summary", Map.of(
                    "total", cameras.size(),
                    "online", 3,
                    "offline", 1,
                    "maintenance", 1));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch camera status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Emergency procedures
     */
    @GetMapping("/emergency-procedures")
    public ResponseEntity<Map<String, Object>> getEmergencyProcedures() {
        try {
            List<Map<String, Object>> procedures = Arrays.asList(
                    createEmergencyProcedure("FIRE", "Fire Emergency Procedure"),
                    createEmergencyProcedure("MEDICAL", "Medical Emergency Procedure"),
                    createEmergencyProcedure("EVACUATION", "Building Evacuation Procedure"),
                    createEmergencyProcedure("SECURITY_THREAT", "Security Threat Response"),
                    createEmergencyProcedure("NATURAL_DISASTER", "Natural Disaster Response"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Emergency procedures retrieved successfully");
            response.put("data", procedures);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch emergency procedures: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Trigger emergency alert
     */
    @PostMapping("/emergency-alert")
    public ResponseEntity<Map<String, Object>> triggerEmergencyAlert(@RequestBody Map<String, Object> alertData) {
        try {
            Map<String, Object> alert = new HashMap<>();
            alert.put("alertId", System.currentTimeMillis());
            alert.put("type", alertData.get("type"));
            alert.put("location", alertData.get("location"));
            alert.put("description", alertData.get("description"));
            alert.put("triggeredBy", alertData.get("triggeredBy"));
            alert.put("triggeredAt", LocalDateTime.now().toString());
            alert.put("status", "ACTIVE");
            alert.put("notificationsSent", Arrays.asList("Security Team", "Management", "Emergency Services"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Emergency alert triggered successfully");
            response.put("data", alert);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to trigger emergency alert: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Security patrol schedules
     */
    @GetMapping("/patrol-schedules")
    public ResponseEntity<Map<String, Object>> getPatrolSchedules() {
        try {
            List<Map<String, Object>> schedules = Arrays.asList(
                    createPatrolSchedule(1L, "Morning Patrol", "06:00", "14:00", "Security Guard A"),
                    createPatrolSchedule(2L, "Evening Patrol", "14:00", "22:00", "Security Guard B"),
                    createPatrolSchedule(3L, "Night Patrol", "22:00", "06:00", "Security Guard C"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Patrol schedules retrieved successfully");
            response.put("data", schedules);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch patrol schedules: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Visitor management
     */
    @GetMapping("/visitors")
    public ResponseEntity<Map<String, Object>> getVisitors() {
        try {
            List<Map<String, Object>> visitors = Arrays.asList(
                    createVisitor(1L, "John Visitor", "Business Meeting", "John Doe", "IN"),
                    createVisitor(2L, "Jane Guest", "Family Visit", "Jane Smith", "OUT"),
                    createVisitor(3L, "Tech Support", "System Maintenance", "IT Department", "IN"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Visitors retrieved successfully");
            response.put("data", visitors);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch visitors: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Register new visitor
     */
    @PostMapping("/visitors")
    public ResponseEntity<Map<String, Object>> registerVisitor(@RequestBody Map<String, Object> visitorData) {
        try {
            Long newId = System.currentTimeMillis();
            Map<String, Object> visitor = new HashMap<>(visitorData);
            visitor.put("id", newId);
            visitor.put("checkInTime", LocalDateTime.now().toString());
            visitor.put("status", "IN");
            visitor.put("visitorPass", "VP_" + newId);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Visitor registered successfully");
            response.put("data", visitor);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to register visitor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Security report generation
     */
    @GetMapping("/reports")
    public ResponseEntity<Map<String, Object>> generateSecurityReport(@RequestParam(required = false) String type,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            Map<String, Object> report = new HashMap<>();
            report.put("reportType", type);
            report.put("period", Map.of("start", startDate, "end", endDate));
            report.put("incidentsSummary", Map.of(
                    "total", 15,
                    "resolved", 12,
                    "pending", 2,
                    "investigating", 1));
            report.put("accessSummary", Map.of(
                    "totalAttempts", 2847,
                    "granted", 2834,
                    "denied", 13,
                    "suspiciousAttempts", 3));
            report.put("cameraSummary", Map.of(
                    "totalCameras", 15,
                    "operational", 14,
                    "maintenance", 1,
                    "uptime", 98.5));
            report.put("generatedAt", LocalDateTime.now().toString());

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Security report generated successfully");
            response.put("data", report);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to generate security report: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Helper methods
    private Map<String, Object> createIncident(Long id, String type, String description, String status) {
        Map<String, Object> incident = new HashMap<>();
        incident.put("id", id);
        incident.put("incidentNumber", "INC_" + String.format("%06d", id));
        incident.put("type", type);
        incident.put("description", description);
        incident.put("status", status);
        incident.put("reportedBy", "Security Officer");
        incident.put("reportedDate", LocalDateTime.now().minusHours(id).toString());
        incident.put("location", "Hotel Premises");
        incident.put("severity", id <= 2 ? "HIGH" : "MEDIUM");
        return incident;
    }

    private Map<String, Object> createAccessLog(Long id, String userName, String userId, String location,
            String status) {
        Map<String, Object> log = new HashMap<>();
        log.put("id", id);
        log.put("userName", userName);
        log.put("userId", userId);
        log.put("location", location);
        log.put("accessTime", LocalDateTime.now().minusMinutes(id * 15).toString());
        log.put("status", status);
        log.put("accessMethod", "KEY_CARD");
        return log;
    }

    private Map<String, Object> createCamera(Long id, String cameraId, String location, String status) {
        Map<String, Object> camera = new HashMap<>();
        camera.put("id", id);
        camera.put("cameraId", cameraId);
        camera.put("location", location);
        camera.put("status", status);
        camera.put("lastPing", LocalDateTime.now().minusMinutes(id).toString());
        camera.put("recordingStatus", status.equals("ONLINE") ? "RECORDING" : "STOPPED");
        return camera;
    }

    private Map<String, Object> createEmergencyProcedure(String type, String title) {
        Map<String, Object> procedure = new HashMap<>();
        procedure.put("type", type);
        procedure.put("title", title);
        procedure.put("steps", Arrays.asList(
                "Assess the situation",
                "Alert security team",
                "Contact emergency services if needed",
                "Follow evacuation procedures",
                "Report to management"));
        procedure.put("emergencyContacts", Arrays.asList(
                Map.of("role", "Security Manager", "phone", "911"),
                Map.of("role", "Fire Department", "phone", "911"),
                Map.of("role", "Medical Emergency", "phone", "911")));
        return procedure;
    }

    private Map<String, Object> createPatrolSchedule(Long id, String shiftName, String startTime, String endTime,
            String assignedTo) {
        Map<String, Object> schedule = new HashMap<>();
        schedule.put("id", id);
        schedule.put("shiftName", shiftName);
        schedule.put("startTime", startTime);
        schedule.put("endTime", endTime);
        schedule.put("assignedTo", assignedTo);
        schedule.put("patrolAreas", Arrays.asList("Lobby", "Parking", "Emergency Exits", "Perimeter"));
        schedule.put("status", "ACTIVE");
        return schedule;
    }

    private Map<String, Object> createVisitor(Long id, String name, String purpose, String hostName, String status) {
        Map<String, Object> visitor = new HashMap<>();
        visitor.put("id", id);
        visitor.put("name", name);
        visitor.put("purpose", purpose);
        visitor.put("hostName", hostName);
        visitor.put("checkInTime", LocalDateTime.now().minusHours(id).toString());
        visitor.put("status", status);
        visitor.put("visitorPass", "VP_" + String.format("%06d", id));
        return visitor;
    }
}
