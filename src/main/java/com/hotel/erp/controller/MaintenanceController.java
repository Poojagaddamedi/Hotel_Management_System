package com.hotel.erp.controller;

import com.hotel.erp.entity.Maintenance;
import com.hotel.erp.repository.MaintenanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/maintenance")
@CrossOrigin(origins = "*")
public class MaintenanceController {

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllRequests() {
        try {
            List<Maintenance> requests = maintenanceRepository.findAll();
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("count", requests.size());
            response.put("data", requests);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch maintenance requests: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getRequestById(@PathVariable Long id) {
        try {
            Optional<Maintenance> request = maintenanceRepository.findById(id);
            Map<String, Object> response = new HashMap<>();
            
            if (request.isPresent()) {
                response.put("status", "success");
                response.put("data", request.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "Maintenance request not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch maintenance request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createRequest(@RequestBody Maintenance maintenance) {
        try {
            if (maintenance.getTicketNo() == null || maintenance.getTicketNo().isEmpty()) {
                String ticketNo = "MNT_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                maintenance.setTicketNo(ticketNo);
            }
            
            Maintenance savedRequest = maintenanceRepository.save(maintenance);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Maintenance request created successfully");
            response.put("data", savedRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to create maintenance request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateRequest(@PathVariable Long id, @RequestBody Maintenance maintenance) {
        try {
            Optional<Maintenance> existingRequest = maintenanceRepository.findById(id);
            Map<String, Object> response = new HashMap<>();
            
            if (existingRequest.isPresent()) {
                Maintenance request = existingRequest.get();
                request.setRoomNo(maintenance.getRoomNo());
                request.setArea(maintenance.getArea());
                request.setIssueType(maintenance.getIssueType());
                request.setPriority(maintenance.getPriority());
                request.setStatus(maintenance.getStatus());
                request.setDescription(maintenance.getDescription());
                request.setReportedBy(maintenance.getReportedBy());
                request.setAssignedTo(maintenance.getAssignedTo());
                request.setEstimatedCost(maintenance.getEstimatedCost());
                request.setActualCost(maintenance.getActualCost());
                request.setVendorName(maintenance.getVendorName());
                request.setVendorContact(maintenance.getVendorContact());
                request.setCompletionNotes(maintenance.getCompletionNotes());
                request.setGuestImpact(maintenance.getGuestImpact());
                request.setEquipmentNeeded(maintenance.getEquipmentNeeded());
                request.setUserId(maintenance.getUserId());
                
                Maintenance updatedRequest = maintenanceRepository.save(request);
                
                response.put("status", "success");
                response.put("message", "Maintenance request updated successfully");
                response.put("data", updatedRequest);
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "Maintenance request not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to update maintenance request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteRequest(@PathVariable Long id) {
        try {
            Optional<Maintenance> request = maintenanceRepository.findById(id);
            Map<String, Object> response = new HashMap<>();
            
            if (request.isPresent()) {
                maintenanceRepository.deleteById(id);
                response.put("status", "success");
                response.put("message", "Maintenance request deleted successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "Maintenance request not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to delete maintenance request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Map<String, Object>> getRequestsByStatus(@PathVariable String status) {
        try {
            List<Maintenance> requests = maintenanceRepository.findByStatus(status);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("count", requests.size());
            response.put("data", requests);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch requests by status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<Map<String, Object>> getRequestsByPriority(@PathVariable String priority) {
        try {
            List<Maintenance> requests = maintenanceRepository.findByPriority(priority);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("count", requests.size());
            response.put("data", requests);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch requests by priority: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/room/{roomNo}")
    public ResponseEntity<Map<String, Object>> getRequestsByRoom(@PathVariable String roomNo) {
        try {
            List<Maintenance> requests = maintenanceRepository.findByRoomNo(roomNo);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("count", requests.size());
            response.put("data", requests);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch requests by room: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/overdue")
    public ResponseEntity<Map<String, Object>> getOverdueRequests() {
        try {
            List<Maintenance> requests = maintenanceRepository.findOverdueRequests(LocalDate.now());
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("count", requests.size());
            response.put("data", requests);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch overdue requests: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/guest-impact")
    public ResponseEntity<Map<String, Object>> getGuestImpactRequests() {
        try {
            List<Maintenance> requests = maintenanceRepository.findActiveGuestImpactRequests();
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("count", requests.size());
            response.put("data", requests);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch guest impact requests: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchRequests(@RequestParam String query) {
        try {
            List<Maintenance> requests = maintenanceRepository.searchMaintenance(query);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("count", requests.size());
            response.put("data", requests);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to search requests: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
