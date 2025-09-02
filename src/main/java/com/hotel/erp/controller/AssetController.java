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
 * Asset Management Controller
 * Handles hotel assets, equipment, furniture, and asset tracking
 */
@RestController
@RequestMapping("/api/assets")
@CrossOrigin(origins = "*")
public class AssetController {

    /**
     * Get all assets
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllAssets() {
        try {
            List<Map<String, Object>> assets = Arrays.asList(
                    createAsset(1L, "AC-001", "Air Conditioner", "HVAC", "LG", "Room 101", "OPERATIONAL"),
                    createAsset(2L, "TV-001", "Smart TV", "ELECTRONICS", "Samsung", "Room 101", "OPERATIONAL"),
                    createAsset(3L, "BED-001", "King Size Bed", "FURNITURE", "Custom", "Room 101", "OPERATIONAL"),
                    createAsset(4L, "FRIDGE-001", "Mini Refrigerator", "APPLIANCES", "LG", "Room 102", "OPERATIONAL"),
                    createAsset(5L, "DESK-001", "Work Desk", "FURNITURE", "Custom", "Room 102", "MAINTENANCE"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Assets retrieved successfully");
            response.put("data", assets);
            response.put("total", assets.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch assets: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get asset by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getAssetById(@PathVariable Long id) {
        try {
            Map<String, Object> asset = createDetailedAsset(id);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Asset retrieved successfully");
            response.put("data", asset);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch asset: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Create new asset
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createAsset(@RequestBody Map<String, Object> assetData) {
        try {
            Long newId = System.currentTimeMillis();
            Map<String, Object> createdAsset = new HashMap<>(assetData);
            createdAsset.put("id", newId);
            createdAsset.put("status", "OPERATIONAL");
            createdAsset.put("acquisitionDate", LocalDate.now().toString());
            createdAsset.put("createdDate", LocalDateTime.now().toString());
            createdAsset.put("lastUpdated", LocalDateTime.now().toString());

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Asset created successfully");
            response.put("data", createdAsset);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to create asset: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Update asset
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateAsset(@PathVariable Long id,
            @RequestBody Map<String, Object> assetData) {
        try {
            Map<String, Object> updatedAsset = new HashMap<>(assetData);
            updatedAsset.put("id", id);
            updatedAsset.put("lastUpdated", LocalDateTime.now().toString());

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Asset updated successfully");
            response.put("data", updatedAsset);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to update asset: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Delete asset
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteAsset(@PathVariable Long id) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Asset deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to delete asset: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get assets by category
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<Map<String, Object>> getAssetsByCategory(@PathVariable String category) {
        try {
            List<Map<String, Object>> assets = Arrays.asList(
                    createAsset(1L, "AC-001", "Air Conditioner", category, "LG", "Room 101", "OPERATIONAL"),
                    createAsset(2L, "AC-002", "Air Conditioner", category, "Samsung", "Room 102", "OPERATIONAL"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Assets retrieved by category successfully");
            response.put("data", assets);
            response.put("category", category);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch assets by category: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get assets by location
     */
    @GetMapping("/location/{location}")
    public ResponseEntity<Map<String, Object>> getAssetsByLocation(@PathVariable String location) {
        try {
            List<Map<String, Object>> assets = Arrays.asList(
                    createAsset(1L, "AC-001", "Air Conditioner", "HVAC", "LG", location, "OPERATIONAL"),
                    createAsset(2L, "TV-001", "Smart TV", "ELECTRONICS", "Samsung", location, "OPERATIONAL"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Assets retrieved by location successfully");
            response.put("data", assets);
            response.put("location", location);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch assets by location: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get assets by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<Map<String, Object>> getAssetsByStatus(@PathVariable String status) {
        try {
            List<Map<String, Object>> assets = Arrays.asList(
                    createAsset(1L, "DESK-001", "Work Desk", "FURNITURE", "Custom", "Room 102", status),
                    createAsset(2L, "CHAIR-001", "Office Chair", "FURNITURE", "Herman Miller", "Room 103", status));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Assets retrieved by status successfully");
            response.put("data", assets);
            response.put("assetStatus", status);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch assets by status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get assets due for maintenance
     */
    @GetMapping("/maintenance-due")
    public ResponseEntity<Map<String, Object>> getMaintenanceDueAssets() {
        try {
            List<Map<String, Object>> assets = Arrays.asList(
                    createMaintenanceDueAsset(1L, "AC-001", "Air Conditioner", "Filter replacement due"),
                    createMaintenanceDueAsset(2L, "ELEVATOR-001", "Passenger Elevator", "Annual inspection due"),
                    createMaintenanceDueAsset(3L, "GENERATOR-001", "Backup Generator", "Routine maintenance due"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Maintenance due assets retrieved successfully");
            response.put("data", assets);
            response.put("total", assets.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch maintenance due assets: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Schedule asset maintenance
     */
    @PostMapping("/{id}/schedule-maintenance")
    public ResponseEntity<Map<String, Object>> scheduleMaintenanceAsset(@PathVariable Long id,
            @RequestBody Map<String, Object> maintenanceData) {
        try {
            Map<String, Object> scheduledMaintenance = new HashMap<>();
            scheduledMaintenance.put("assetId", id);
            scheduledMaintenance.put("maintenanceType", maintenanceData.get("maintenanceType"));
            scheduledMaintenance.put("scheduledDate", maintenanceData.get("scheduledDate"));
            scheduledMaintenance.put("assignedTo", maintenanceData.get("assignedTo"));
            scheduledMaintenance.put("estimatedCost", maintenanceData.get("estimatedCost"));
            scheduledMaintenance.put("priority", maintenanceData.get("priority"));
            scheduledMaintenance.put("status", "SCHEDULED");
            scheduledMaintenance.put("createdDate", LocalDateTime.now().toString());

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Asset maintenance scheduled successfully");
            response.put("data", scheduledMaintenance);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to schedule asset maintenance: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get asset history
     */
    @GetMapping("/{id}/history")
    public ResponseEntity<Map<String, Object>> getAssetHistory(@PathVariable Long id) {
        try {
            List<Map<String, Object>> history = Arrays.asList(
                    createHistoryRecord("ACQUIRED", "Asset acquired and installed", LocalDateTime.now().minusYears(1)),
                    createHistoryRecord("MAINTENANCE", "Routine maintenance performed",
                            LocalDateTime.now().minusMonths(6)),
                    createHistoryRecord("REPAIR", "Minor repair completed", LocalDateTime.now().minusMonths(2)),
                    createHistoryRecord("INSPECTION", "Safety inspection passed", LocalDateTime.now().minusWeeks(1)));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Asset history retrieved successfully");
            response.put("data", history);
            response.put("assetId", id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch asset history: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Generate asset report
     */
    @GetMapping("/report")
    public ResponseEntity<Map<String, Object>> generateAssetReport(@RequestParam(required = false) String category,
            @RequestParam(required = false) String location) {
        try {
            Map<String, Object> report = new HashMap<>();
            report.put("totalAssets", 150);
            report.put("operationalAssets", 142);
            report.put("maintenanceAssets", 6);
            report.put("retiredAssets", 2);
            report.put("totalValue", 2500000.00);
            report.put("depreciationValue", 450000.00);
            report.put("currentValue", 2050000.00);

            report.put("categoryBreakdown", Arrays.asList(
                    Map.of("category", "ELECTRONICS", "count", 45, "value", 750000.00),
                    Map.of("category", "FURNITURE", "count", 60, "value", 900000.00),
                    Map.of("category", "HVAC", "count", 25, "value", 600000.00),
                    Map.of("category", "APPLIANCES", "count", 20, "value", 300000.00)));

            report.put("maintenanceSchedule", Arrays.asList(
                    Map.of("assetId", "AC-001", "dueDate", LocalDate.now().plusDays(7).toString()),
                    Map.of("assetId", "ELEVATOR-001", "dueDate", LocalDate.now().plusDays(14).toString())));

            report.put("generatedDate", LocalDateTime.now().toString());
            report.put("filters", Map.of("category", category, "location", location));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Asset report generated successfully");
            response.put("data", report);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to generate asset report: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Asset valuation
     */
    @GetMapping("/valuation")
    public ResponseEntity<Map<String, Object>> getAssetValuation() {
        try {
            Map<String, Object> valuation = new HashMap<>();
            valuation.put("totalAcquisitionCost", 2500000.00);
            valuation.put("totalDepreciation", 450000.00);
            valuation.put("currentBookValue", 2050000.00);
            valuation.put("marketValue", 1950000.00);
            valuation.put("insuranceValue", 2200000.00);
            valuation.put("depreciationRate", 18.0);
            valuation.put("averageAssetAge", 2.5);
            valuation.put("valuationDate", LocalDate.now().toString());

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Asset valuation retrieved successfully");
            response.put("data", valuation);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch asset valuation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Helper methods
    private Map<String, Object> createAsset(Long id, String assetCode, String assetName, String category,
            String brand, String location, String status) {
        Map<String, Object> asset = new HashMap<>();
        asset.put("id", id);
        asset.put("assetCode", assetCode);
        asset.put("assetName", assetName);
        asset.put("category", category);
        asset.put("brand", brand);
        asset.put("model", "Model " + id);
        asset.put("location", location);
        asset.put("status", status);
        asset.put("acquisitionDate", LocalDate.now().minusMonths(6).toString());
        asset.put("acquisitionCost", 15000.00 + (id * 1000));
        asset.put("currentValue", 12000.00 + (id * 800));
        asset.put("warrantyExpiryDate", LocalDate.now().plusYears(1).toString());
        return asset;
    }

    private Map<String, Object> createDetailedAsset(Long id) {
        Map<String, Object> asset = new HashMap<>();
        asset.put("id", id);
        asset.put("assetCode", "AC-" + String.format("%03d", id));
        asset.put("assetName", "Air Conditioner Unit");
        asset.put("category", "HVAC");
        asset.put("brand", "LG");
        asset.put("model", "Dual Inverter LSA5NP5A");
        asset.put("serialNumber", "LG" + System.currentTimeMillis());
        asset.put("location", "Room 101");
        asset.put("status", "OPERATIONAL");
        asset.put("acquisitionDate", LocalDate.now().minusYears(1).toString());
        asset.put("acquisitionCost", 45000.00);
        asset.put("currentValue", 38000.00);
        asset.put("depreciationRate", 15.5);
        asset.put("warranty", Map.of(
                "provider", "LG Electronics",
                "startDate", LocalDate.now().minusYears(1).toString(),
                "endDate", LocalDate.now().plusYears(1).toString(),
                "coverage", "Parts and Labor"));
        asset.put("specifications", Map.of(
                "capacity", "1.5 Ton",
                "powerConsumption", "1200W",
                "energyRating", "5 Star",
                "refrigerant", "R-32"));
        asset.put("lastMaintenanceDate", LocalDate.now().minusMonths(3).toString());
        asset.put("nextMaintenanceDate", LocalDate.now().plusMonths(3).toString());
        return asset;
    }

    private Map<String, Object> createMaintenanceDueAsset(Long id, String assetCode, String assetName, String reason) {
        Map<String, Object> asset = new HashMap<>();
        asset.put("id", id);
        asset.put("assetCode", assetCode);
        asset.put("assetName", assetName);
        asset.put("maintenanceReason", reason);
        asset.put("dueDate", LocalDate.now().plusDays(3).toString());
        asset.put("priority", "HIGH");
        asset.put("estimatedCost", 2500.00);
        asset.put("lastMaintenanceDate", LocalDate.now().minusMonths(6).toString());
        return asset;
    }

    private Map<String, Object> createHistoryRecord(String action, String description, LocalDateTime timestamp) {
        Map<String, Object> record = new HashMap<>();
        record.put("action", action);
        record.put("description", description);
        record.put("timestamp", timestamp.toString());
        record.put("performedBy", "System Admin");
        record.put("cost", action.equals("REPAIR") ? 1500.00 : 0.00);
        return record;
    }
}
