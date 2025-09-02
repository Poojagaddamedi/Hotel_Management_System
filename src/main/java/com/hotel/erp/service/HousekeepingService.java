package com.hotel.erp.service;

import com.hotel.erp.dto.HousekeepingDTO;
import com.hotel.erp.entity.Housekeeping;
import com.hotel.erp.repository.HousekeepingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class HousekeepingService {

    private static final Logger logger = LoggerFactory.getLogger(HousekeepingService.class);

    @Autowired
    private HousekeepingRepository housekeepingRepository;

    /**
     * Create a new housekeeping task
     */
    public Housekeeping createTask(HousekeepingDTO taskDTO) {
        logger.info("Creating housekeeping task for room: {}", taskDTO.getRoomNo());

        try {
            // Validate room number is not empty
            if (taskDTO.getRoomNo() == null || taskDTO.getRoomNo().trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room number is required");
            }

            // Validate status is not empty
            if (taskDTO.getStatus() == null || taskDTO.getStatus().trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status is required");
            }

            // Validate task date is not in the future
            if (taskDTO.getTaskDate() != null && taskDTO.getTaskDate().isAfter(LocalDate.now())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task date cannot be in the future");
            }

            // Create task
            Housekeeping task = new Housekeeping();
            task.setRoomNo(taskDTO.getRoomNo());
            task.setStatus(taskDTO.getStatus());
            task.setAssignedTo(taskDTO.getAssignedTo());
            task.setTaskDate(taskDTO.getTaskDate() != null ? taskDTO.getTaskDate() : LocalDate.now());
            task.setRemarks(taskDTO.getRemarks());
            task.setUserId(taskDTO.getUserId());

            Housekeeping savedTask = housekeepingRepository.save(task);

            logger.info("Housekeeping task created successfully with ID: {}", savedTask.getId());
            return savedTask;

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error creating housekeeping task: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating housekeeping task");
        }
    }

    /**
     * Get tasks by room number
     */
    public List<Housekeeping> getTasksByRoom(String roomNo) {
        try {
            List<Housekeeping> tasks = housekeepingRepository.findByRoomNo(roomNo);
            logger.info("Retrieved {} tasks for room: {}", tasks.size(), roomNo);
            return tasks;
        } catch (Exception e) {
            logger.error("Error retrieving tasks: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Get tasks by status
     */
    public List<Housekeeping> getTasksByStatus(String status) {
        try {
            List<Housekeeping> tasks = housekeepingRepository.findByStatus(status);
            logger.info("Retrieved {} tasks with status: {}", tasks.size(), status);
            return tasks;
        } catch (Exception e) {
            logger.error("Error retrieving tasks: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Get tasks by assigned staff
     */
    public List<Housekeeping> getTasksByAssigned(String assignedTo) {
        try {
            List<Housekeeping> tasks = housekeepingRepository.findByAssignedTo(assignedTo);
            logger.info("Retrieved {} tasks assigned to: {}", tasks.size(), assignedTo);
            return tasks;
        } catch (Exception e) {
            logger.error("Error retrieving tasks: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Get task summary
     */
    public Map<String, Object> getTaskSummary() {
        try {
            Map<String, Object> summary = new HashMap<>();

            List<Housekeeping> allTasks = getAllTasks();
            List<Housekeeping> pendingTasks = getTasksByStatus("Pending");
            List<Housekeeping> completedTasks = getTasksByStatus("Completed");
            List<Housekeeping> todayTasks = getTodayTasks();

            summary.put("totalTasks", allTasks.size());
            summary.put("pendingTasks", pendingTasks.size());
            summary.put("completedTasks", completedTasks.size());
            summary.put("todayTasks", todayTasks.size());

            logger.info("Retrieved task summary");
            return summary;

        } catch (Exception e) {
            logger.error("Error getting task summary: {}", e.getMessage(), e);
            Map<String, Object> summary = new HashMap<>();
            summary.put("totalTasks", 0);
            summary.put("pendingTasks", 0);
            summary.put("completedTasks", 0);
            summary.put("todayTasks", 0);
            return summary;
        }
    }

    /**
     * Get all tasks
     */
    public List<Housekeeping> getAllTasks() {
        try {
            List<Housekeeping> tasks = housekeepingRepository.findAll();
            logger.info("Retrieved {} total tasks", tasks.size());
            return tasks;
        } catch (Exception e) {
            logger.error("Error retrieving all tasks: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Update a task
     */
    public Housekeeping updateTask(Long taskId, HousekeepingDTO taskDTO) {
        logger.info("Updating task with ID: {}", taskId);

        try {
            Optional<Housekeeping> taskOpt = housekeepingRepository.findById(taskId);
            if (taskOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
            }

            Housekeeping task = taskOpt.get();

            // Update fields
            if (taskDTO.getRoomNo() != null) {
                task.setRoomNo(taskDTO.getRoomNo());
            }
            if (taskDTO.getStatus() != null) {
                task.setStatus(taskDTO.getStatus());
            }
            if (taskDTO.getAssignedTo() != null) {
                task.setAssignedTo(taskDTO.getAssignedTo());
            }
            if (taskDTO.getTaskDate() != null) {
                task.setTaskDate(taskDTO.getTaskDate());
            }
            if (taskDTO.getRemarks() != null) {
                task.setRemarks(taskDTO.getRemarks());
            }
            if (taskDTO.getUserId() != null) {
                task.setUserId(taskDTO.getUserId());
            }

            Housekeeping updatedTask = housekeepingRepository.save(task);

            logger.info("Task updated successfully with ID: {}", updatedTask.getId());
            return updatedTask;

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error updating task: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating task");
        }
    }

    /**
     * Delete a task
     */
    public void deleteTask(Long taskId) {
        logger.info("Deleting task with ID: {}", taskId);

        try {
            if (!housekeepingRepository.existsById(taskId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
            }

            housekeepingRepository.deleteById(taskId);
            logger.info("Task deleted successfully with ID: {}", taskId);

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error deleting task: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting task");
        }
    }

    /**
     * Search tasks
     */
    public List<Housekeeping> searchTasks(String query) {
        try {
            List<Housekeeping> tasks = housekeepingRepository.searchTasks(query);
            logger.info("Found {} tasks matching query: {}", tasks.size(), query);
            return tasks;
        } catch (Exception e) {
            logger.error("Error searching tasks: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Get tasks by date range
     */
    public List<Housekeeping> getTasksByDateRange(String startDate, String endDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate, formatter);

            List<Housekeeping> tasks = housekeepingRepository.findByTaskDateBetween(start, end);
            logger.info("Retrieved {} tasks between {} and {}", tasks.size(), startDate, endDate);
            return tasks;
        } catch (Exception e) {
            logger.error("Error retrieving tasks by date range: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Update task status
     */
    public Housekeeping updateTaskStatus(Long taskId, String status) {
        logger.info("Updating task status for ID: {} to: {}", taskId, status);

        try {
            Optional<Housekeeping> taskOpt = housekeepingRepository.findById(taskId);
            if (taskOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
            }

            Housekeeping task = taskOpt.get();
            task.setStatus(status);

            Housekeeping updatedTask = housekeepingRepository.save(task);

            logger.info("Task status updated successfully for ID: {}", updatedTask.getId());
            return updatedTask;

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error updating task status: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating task status");
        }
    }

    /**
     * Assign task to staff
     */
    public Housekeeping assignTask(Long taskId, String assignedTo) {
        logger.info("Assigning task ID: {} to: {}", taskId, assignedTo);

        try {
            Optional<Housekeeping> taskOpt = housekeepingRepository.findById(taskId);
            if (taskOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
            }

            Housekeeping task = taskOpt.get();
            task.setAssignedTo(assignedTo);

            Housekeeping updatedTask = housekeepingRepository.save(task);

            logger.info("Task assigned successfully for ID: {}", updatedTask.getId());
            return updatedTask;

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error assigning task: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error assigning task");
        }
    }

    /**
     * Get tasks for today
     */
    public List<Housekeeping> getTodayTasks() {
        try {
            List<Housekeeping> tasks = housekeepingRepository.findByTaskDate(LocalDate.now());
            logger.info("Retrieved {} tasks for today", tasks.size());
            return tasks;
        } catch (Exception e) {
            logger.error("Error retrieving today's tasks: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Get pending tasks
     */
    public List<Housekeeping> getPendingTasks() {
        try {
            List<Housekeeping> tasks = getTasksByStatus("Pending");
            logger.info("Retrieved {} pending tasks", tasks.size());
            return tasks;
        } catch (Exception e) {
            logger.error("Error retrieving pending tasks: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Get housekeeping rooms summary (compatibility endpoint)
     */
    public Map<String, Object> getHousekeepingRoomsSummary() {
        try {
            Map<String, Object> summary = new HashMap<>();

            // Get room statistics from housekeeping perspective
            List<Housekeeping> allTasks = getAllTasks();
            Map<String, Integer> roomStatusCount = new HashMap<>();

            // Count tasks by status
            for (Housekeeping task : allTasks) {
                String status = task.getStatus();
                roomStatusCount.put(status, roomStatusCount.getOrDefault(status, 0) + 1);
            }

            summary.put("totalRooms", allTasks.size());
            summary.put("statusBreakdown", roomStatusCount);
            summary.put("pendingTasks", roomStatusCount.getOrDefault("Pending", 0));
            summary.put("completedTasks", roomStatusCount.getOrDefault("Completed", 0));
            summary.put("inProgressTasks", roomStatusCount.getOrDefault("In Progress", 0));

            logger.info("Retrieved housekeeping rooms summary");
            return summary;

        } catch (Exception e) {
            logger.error("Error getting housekeeping rooms summary: {}", e.getMessage(), e);
            Map<String, Object> summary = new HashMap<>();
            summary.put("totalRooms", 0);
            summary.put("statusBreakdown", new HashMap<>());
            summary.put("pendingTasks", 0);
            summary.put("completedTasks", 0);
            summary.put("inProgressTasks", 0);
            return summary;
        }
    }

    /**
     * Get housekeeping assignments
     */
    public Map<String, Object> getHousekeepingAssignments() {
        try {
            Map<String, Object> assignments = new HashMap<>();

            List<Housekeeping> allTasks = getAllTasks();
            Map<String, Integer> assignmentCount = new HashMap<>();
            Map<String, List<String>> assignmentDetails = new HashMap<>();

            // Group tasks by assigned staff
            for (Housekeeping task : allTasks) {
                String assignedTo = task.getAssignedTo();
                if (assignedTo != null && !assignedTo.trim().isEmpty()) {
                    assignmentCount.put(assignedTo, assignmentCount.getOrDefault(assignedTo, 0) + 1);

                    List<String> roomList = assignmentDetails.getOrDefault(assignedTo, new ArrayList<>());
                    roomList.add(task.getRoomNo());
                    assignmentDetails.put(assignedTo, roomList);
                }
            }

            assignments.put("totalAssignments", allTasks.size());
            assignments.put("staffWorkload", assignmentCount);
            assignments.put("assignmentDetails", assignmentDetails);
            assignments.put("unassignedTasks", (int) allTasks.stream()
                    .filter(task -> task.getAssignedTo() == null || task.getAssignedTo().trim().isEmpty()).count());

            logger.info("Retrieved housekeeping assignments");
            return assignments;

        } catch (Exception e) {
            logger.error("Error getting housekeeping assignments: {}", e.getMessage(), e);
            Map<String, Object> assignments = new HashMap<>();
            assignments.put("totalAssignments", 0);
            assignments.put("staffWorkload", new HashMap<>());
            assignments.put("assignmentDetails", new HashMap<>());
            assignments.put("unassignedTasks", 0);
            return assignments;
        }
    }
}