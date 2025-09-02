package com.hotel.erp.controller;

import com.hotel.erp.dto.HousekeepingDTO;
import com.hotel.erp.entity.Housekeeping;
import com.hotel.erp.service.HousekeepingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/housekeeping")
@CrossOrigin(origins = "*")
public class HousekeepingController {

    private static final Logger logger = LoggerFactory.getLogger(HousekeepingController.class);

    @Autowired
    private HousekeepingService housekeepingService;

    /**
     * Create a new housekeeping task
     */
    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody HousekeepingDTO taskDTO) {
        try {
            logger.info("Creating housekeeping task for room: {}", taskDTO.getRoomNo());
            Housekeeping task = housekeepingService.createTask(taskDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(task);
        } catch (Exception e) {
            logger.error("Error creating housekeeping task: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error creating housekeeping task: " + e.getMessage());
        }
    }

    /**
     * Get tasks by room number
     */
    @GetMapping("/room/{roomNo}")
    public ResponseEntity<List<Housekeeping>> getTasksByRoom(@PathVariable String roomNo) {
        try {
            List<Housekeeping> tasks = housekeepingService.getTasksByRoom(roomNo);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            logger.error("Error retrieving tasks: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get tasks by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Housekeeping>> getTasksByStatus(@PathVariable String status) {
        try {
            List<Housekeeping> tasks = housekeepingService.getTasksByStatus(status);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            logger.error("Error retrieving tasks: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get tasks by assigned staff
     */
    @GetMapping("/assigned/{assignedTo}")
    public ResponseEntity<List<Housekeeping>> getTasksByAssigned(@PathVariable String assignedTo) {
        try {
            List<Housekeeping> tasks = housekeepingService.getTasksByAssigned(assignedTo);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            logger.error("Error retrieving tasks: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get task summary
     */
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getTaskSummary() {
        try {
            Map<String, Object> summary = housekeepingService.getTaskSummary();
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            logger.error("Error getting task summary: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get all tasks
     */
    @GetMapping
    public ResponseEntity<List<Housekeeping>> getAllTasks() {
        try {
            List<Housekeeping> tasks = housekeepingService.getAllTasks();
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            logger.error("Error retrieving all tasks: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update a task
     */
    @PutMapping("/{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable Long taskId, @RequestBody HousekeepingDTO taskDTO) {
        try {
            Housekeeping updatedTask = housekeepingService.updateTask(taskId, taskDTO);
            return ResponseEntity.ok(updatedTask);
        } catch (Exception e) {
            logger.error("Error updating task: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error updating task: " + e.getMessage());
        }
    }

    /**
     * Delete a task
     */
    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId) {
        try {
            housekeepingService.deleteTask(taskId);
            return ResponseEntity.ok().body("Task deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting task: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error deleting task: " + e.getMessage());
        }
    }

    /**
     * Search tasks
     */
    @GetMapping("/search")
    public ResponseEntity<List<Housekeeping>> searchTasks(@RequestParam String query) {
        try {
            List<Housekeeping> tasks = housekeepingService.searchTasks(query);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            logger.error("Error searching tasks: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get tasks by date range
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<Housekeeping>> getTasksByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            List<Housekeeping> tasks = housekeepingService.getTasksByDateRange(startDate, endDate);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            logger.error("Error retrieving tasks by date range: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update task status
     */
    @PutMapping("/{taskId}/status")
    public ResponseEntity<?> updateTaskStatus(@PathVariable Long taskId, @RequestParam String status) {
        try {
            Housekeeping updatedTask = housekeepingService.updateTaskStatus(taskId, status);
            return ResponseEntity.ok(updatedTask);
        } catch (Exception e) {
            logger.error("Error updating task status: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error updating task status: " + e.getMessage());
        }
    }

    /**
     * Assign task to staff
     */
    @PutMapping("/{taskId}/assign")
    public ResponseEntity<?> assignTask(@PathVariable Long taskId, @RequestParam String assignedTo) {
        try {
            Housekeeping updatedTask = housekeepingService.assignTask(taskId, assignedTo);
            return ResponseEntity.ok(updatedTask);
        } catch (Exception e) {
            logger.error("Error assigning task: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error assigning task: " + e.getMessage());
        }
    }

    /**
     * Get tasks for today
     */
    @GetMapping("/today")
    public ResponseEntity<List<Housekeeping>> getTodayTasks() {
        try {
            List<Housekeeping> tasks = housekeepingService.getTodayTasks();
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            logger.error("Error retrieving today's tasks: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get pending tasks
     */
    @GetMapping("/pending")
    public ResponseEntity<List<Housekeeping>> getPendingTasks() {
        try {
            List<Housekeeping> tasks = housekeepingService.getPendingTasks();
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            logger.error("Error retrieving pending tasks: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get rooms for housekeeping (compatibility endpoint)
     */
    @GetMapping("/rooms")
    public ResponseEntity<?> getHousekeepingRooms() {
        try {
            // Return a summary of rooms and their housekeeping status
            Map<String, Object> response = housekeepingService.getHousekeepingRoomsSummary();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error retrieving housekeeping rooms: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get housekeeping assignments
     */
    @GetMapping("/assignments")
    public ResponseEntity<?> getHousekeepingAssignments() {
        try {
            Map<String, Object> assignments = housekeepingService.getHousekeepingAssignments();
            return ResponseEntity.ok(assignments);
        } catch (Exception e) {
            logger.error("Error retrieving housekeeping assignments: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}