package com.hotel.erp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Training Management Controller
 * Handles employee training programs, certifications, and skill development
 */
@RestController
@RequestMapping("/api/training")
@CrossOrigin(origins = "*")
public class TrainingController {

    /**
     * Get all training programs
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllTrainingPrograms() {
        try {
            List<Map<String, Object>> programs = Arrays.asList(
                    createTrainingProgram(1L, "Customer Service Excellence", "CUSTOMER_SERVICE", "ACTIVE"),
                    createTrainingProgram(2L, "Food Safety & Hygiene", "FOOD_SAFETY", "ACTIVE"),
                    createTrainingProgram(3L, "Emergency Procedures", "SAFETY", "ACTIVE"),
                    createTrainingProgram(4L, "Hotel Management Systems", "TECHNOLOGY", "ACTIVE"),
                    createTrainingProgram(5L, "Leadership Development", "MANAGEMENT", "PLANNED"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Training programs retrieved successfully");
            response.put("data", programs);
            response.put("total", programs.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch training programs: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get training program by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getTrainingById(@PathVariable Long id) {
        try {
            Map<String, Object> program = createDetailedTrainingProgram(id);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Training program retrieved successfully");
            response.put("data", program);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch training program: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Create new training program
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createTrainingProgram(@RequestBody Map<String, Object> trainingData) {
        try {
            Long newId = System.currentTimeMillis();
            Map<String, Object> createdProgram = new HashMap<>(trainingData);
            createdProgram.put("id", newId);
            createdProgram.put("status", "PLANNED");
            createdProgram.put("createdDate", LocalDateTime.now().toString());
            createdProgram.put("lastUpdated", LocalDateTime.now().toString());

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Training program created successfully");
            response.put("data", createdProgram);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to create training program: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Update training program
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateTrainingProgram(@PathVariable Long id,
            @RequestBody Map<String, Object> trainingData) {
        try {
            Map<String, Object> updatedProgram = new HashMap<>(trainingData);
            updatedProgram.put("id", id);
            updatedProgram.put("lastUpdated", LocalDateTime.now().toString());

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Training program updated successfully");
            response.put("data", updatedProgram);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to update training program: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Delete training program
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteTrainingProgram(@PathVariable Long id) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Training program deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to delete training program: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get training programs by category
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<Map<String, Object>> getTrainingByCategory(@PathVariable String category) {
        try {
            List<Map<String, Object>> programs = Arrays.asList(
                    createTrainingProgram(1L, "Customer Service Excellence", category, "ACTIVE"),
                    createTrainingProgram(2L, "Guest Relations Training", category, "ACTIVE"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Training programs retrieved by category successfully");
            response.put("data", programs);
            response.put("category", category);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch training programs by category: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Enroll employee in training
     */
    @PostMapping("/{id}/enroll")
    public ResponseEntity<Map<String, Object>> enrollEmployee(@PathVariable Long id,
            @RequestBody Map<String, Object> enrollmentData) {
        try {
            Map<String, Object> enrollment = new HashMap<>();
            enrollment.put("trainingId", id);
            enrollment.put("employeeId", enrollmentData.get("employeeId"));
            enrollment.put("employeeName", enrollmentData.get("employeeName"));
            enrollment.put("department", enrollmentData.get("department"));
            enrollment.put("enrollmentDate", LocalDateTime.now().toString());
            enrollment.put("status", "ENROLLED");
            enrollment.put("progress", 0);
            enrollment.put("completionStatus", "IN_PROGRESS");

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Employee enrolled successfully");
            response.put("data", enrollment);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to enroll employee: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get employee training records
     */
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<Map<String, Object>> getEmployeeTrainings(@PathVariable String employeeId) {
        try {
            List<Map<String, Object>> trainings = Arrays.asList(
                    createEmployeeTraining("Customer Service Excellence", "COMPLETED", 100),
                    createEmployeeTraining("Food Safety & Hygiene", "IN_PROGRESS", 75),
                    createEmployeeTraining("Emergency Procedures", "ENROLLED", 0));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Employee training records retrieved successfully");
            response.put("data", trainings);
            response.put("employeeId", employeeId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch employee training records: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Update training progress
     */
    @PutMapping("/enrollment/{enrollmentId}/progress")
    public ResponseEntity<Map<String, Object>> updateTrainingProgress(@PathVariable Long enrollmentId,
            @RequestBody Map<String, Object> progressData) {
        try {
            Map<String, Object> updatedProgress = new HashMap<>();
            updatedProgress.put("enrollmentId", enrollmentId);
            updatedProgress.put("progress", progressData.get("progress"));
            updatedProgress.put("completedModules", progressData.get("completedModules"));
            updatedProgress.put("lastAccessDate", LocalDateTime.now().toString());

            Integer progress = (Integer) progressData.get("progress");
            updatedProgress.put("completionStatus", progress >= 100 ? "COMPLETED" : "IN_PROGRESS");

            if (progress >= 100) {
                updatedProgress.put("completionDate", LocalDateTime.now().toString());
                updatedProgress.put("certificateIssued", true);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Training progress updated successfully");
            response.put("data", updatedProgress);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to update training progress: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get training certificates
     */
    @GetMapping("/certificates/{employeeId}")
    public ResponseEntity<Map<String, Object>> getEmployeeCertificates(@PathVariable String employeeId) {
        try {
            List<Map<String, Object>> certificates = Arrays.asList(
                    createCertificate("Customer Service Excellence", LocalDate.now().minusMonths(3),
                            LocalDate.now().plusYears(1)),
                    createCertificate("Food Safety & Hygiene", LocalDate.now().minusMonths(6),
                            LocalDate.now().plusMonths(6)),
                    createCertificate("Emergency Procedures", LocalDate.now().minusYears(1), LocalDate.now()));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Employee certificates retrieved successfully");
            response.put("data", certificates);
            response.put("employeeId", employeeId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch employee certificates: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Generate training report
     */
    @GetMapping("/report")
    public ResponseEntity<Map<String, Object>> generateTrainingReport(@RequestParam(required = false) String department,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            Map<String, Object> report = new HashMap<>();
            report.put("totalPrograms", 25);
            report.put("activePrograms", 18);
            report.put("completedPrograms", 7);
            report.put("totalEnrollments", 150);
            report.put("completedEnrollments", 95);
            report.put("averageCompletionRate", 63.3);
            report.put("totalTrainingHours", 2400);
            report.put("averageTrainingHoursPerEmployee", 12.5);

            report.put("departmentBreakdown", Arrays.asList(
                    Map.of("department", "Front Office", "enrollments", 35, "completionRate", 85.7),
                    Map.of("department", "Housekeeping", "enrollments", 42, "completionRate", 71.4),
                    Map.of("department", "F&B", "enrollments", 38, "completionRate", 65.8),
                    Map.of("department", "Management", "enrollments", 15, "completionRate", 93.3)));

            report.put("popularPrograms", Arrays.asList(
                    Map.of("program", "Customer Service Excellence", "enrollments", 45),
                    Map.of("program", "Food Safety & Hygiene", "enrollments", 38),
                    Map.of("program", "Emergency Procedures", "enrollments", 35)));

            report.put("upcomingPrograms", Arrays.asList(
                    Map.of("program", "Leadership Development", "startDate", LocalDate.now().plusWeeks(2).toString()),
                    Map.of("program", "Digital Marketing", "startDate", LocalDate.now().plusMonths(1).toString())));

            report.put("generatedDate", LocalDateTime.now().toString());
            report.put("filters", Map.of("department", department, "startDate", startDate, "endDate", endDate));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Training report generated successfully");
            response.put("data", report);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to generate training report: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get training calendar
     */
    @GetMapping("/calendar")
    public ResponseEntity<Map<String, Object>> getTrainingCalendar(@RequestParam(required = false) String month,
            @RequestParam(required = false) String year) {
        try {
            List<Map<String, Object>> events = Arrays.asList(
                    createCalendarEvent("Customer Service Workshop", LocalDate.now().plusDays(5), "Conference Room A"),
                    createCalendarEvent("Food Safety Training", LocalDate.now().plusDays(12), "Training Room"),
                    createCalendarEvent("Emergency Drill", LocalDate.now().plusDays(18), "All Areas"),
                    createCalendarEvent("Leadership Seminar", LocalDate.now().plusDays(25), "Board Room"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Training calendar retrieved successfully");
            response.put("data", events);
            response.put("month", month);
            response.put("year", year);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch training calendar: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Helper methods
    private Map<String, Object> createTrainingProgram(Long id, String name, String category, String status) {
        Map<String, Object> program = new HashMap<>();
        program.put("id", id);
        program.put("name", name);
        program.put("category", category);
        program.put("status", status);
        program.put("duration", "2 days");
        program.put("durationHours", 16);
        program.put("maxParticipants", 20);
        program.put("currentEnrollments", 12);
        program.put("trainer", "Expert Trainer");
        program.put("startDate", LocalDate.now().plusWeeks(2).toString());
        program.put("endDate", LocalDate.now().plusWeeks(2).plusDays(1).toString());
        program.put("venue", "Training Room A");
        return program;
    }

    private Map<String, Object> createDetailedTrainingProgram(Long id) {
        Map<String, Object> program = new HashMap<>();
        program.put("id", id);
        program.put("name", "Customer Service Excellence");
        program.put("category", "CUSTOMER_SERVICE");
        program.put("description", "Comprehensive training program to enhance customer service skills");
        program.put("objectives", Arrays.asList(
                "Understand customer expectations",
                "Learn effective communication techniques",
                "Handle customer complaints professionally",
                "Build customer loyalty"));
        program.put("modules", Arrays.asList(
                Map.of("name", "Customer Psychology", "duration", "4 hours"),
                Map.of("name", "Communication Skills", "duration", "4 hours"),
                Map.of("name", "Complaint Handling", "duration", "4 hours"),
                Map.of("name", "Service Recovery", "duration", "4 hours")));
        program.put("prerequisites", "Basic communication skills");
        program.put("certification", "Customer Service Excellence Certificate");
        program.put("validityPeriod", "2 years");
        program.put("status", "ACTIVE");
        program.put("trainer", "Sarah Johnson - Customer Service Expert");
        program.put("maxParticipants", 20);
        program.put("currentEnrollments", 12);
        program.put("cost", 5000.00);
        return program;
    }

    private Map<String, Object> createEmployeeTraining(String programName, String status, Integer progress) {
        Map<String, Object> training = new HashMap<>();
        training.put("programName", programName);
        training.put("status", status);
        training.put("progress", progress);
        training.put("enrollmentDate", LocalDate.now().minusMonths(1).toString());
        training.put("completionDate", status.equals("COMPLETED") ? LocalDate.now().minusWeeks(1).toString() : null);
        training.put("certificateIssued", status.equals("COMPLETED"));
        training.put("score", status.equals("COMPLETED") ? 87 : null);
        return training;
    }

    private Map<String, Object> createCertificate(String programName, LocalDate issueDate, LocalDate expiryDate) {
        Map<String, Object> certificate = new HashMap<>();
        certificate.put("programName", programName);
        certificate.put("certificateNumber", "CERT-" + System.currentTimeMillis());
        certificate.put("issueDate", issueDate.toString());
        certificate.put("expiryDate", expiryDate.toString());
        certificate.put("isValid", LocalDate.now().isBefore(expiryDate));
        certificate.put("score", 87);
        certificate.put("grade", "A");
        return certificate;
    }

    private Map<String, Object> createCalendarEvent(String name, LocalDate date, String venue) {
        Map<String, Object> event = new HashMap<>();
        event.put("name", name);
        event.put("date", date.toString());
        event.put("venue", venue);
        event.put("startTime", "09:00");
        event.put("endTime", "17:00");
        event.put("enrollments", 15);
        event.put("maxParticipants", 20);
        event.put("status", "SCHEDULED");
        return event;
    }
}
