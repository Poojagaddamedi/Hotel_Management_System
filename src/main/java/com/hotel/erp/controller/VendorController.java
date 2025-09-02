package com.hotel.erp.controller;

import com.hotel.erp.entity.Vendor;
import com.hotel.erp.repository.VendorRepository;
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
@RequestMapping("/api/vendors")
@CrossOrigin(origins = "*")
public class VendorController {

    @Autowired
    private VendorRepository vendorRepository;

    /**
     * Get all vendors
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllVendors() {
        try {
            List<Vendor> vendors = vendorRepository.findAll();
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("count", vendors.size());
            response.put("data", vendors);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch vendors: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get vendor by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getVendorById(@PathVariable Long id) {
        try {
            Optional<Vendor> vendor = vendorRepository.findById(id);
            Map<String, Object> response = new HashMap<>();

            if (vendor.isPresent()) {
                response.put("status", "success");
                response.put("data", vendor.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "Vendor not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch vendor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Create new vendor
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createVendor(@RequestBody Vendor vendor) {
        try {
            // Generate vendor code if not provided
            if (vendor.getVendorCode() == null || vendor.getVendorCode().isEmpty()) {
                String vendorCode = "VND_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                vendor.setVendorCode(vendorCode);
            }

            Vendor savedVendor = vendorRepository.save(vendor);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Vendor created successfully");
            response.put("data", savedVendor);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to create vendor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Update vendor
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateVendor(@PathVariable Long id, @RequestBody Vendor vendor) {
        try {
            Optional<Vendor> existingVendor = vendorRepository.findById(id);
            Map<String, Object> response = new HashMap<>();

            if (existingVendor.isPresent()) {
                Vendor vendorToUpdate = existingVendor.get();
                vendorToUpdate.setVendorName(vendor.getVendorName());
                vendorToUpdate.setVendorType(vendor.getVendorType());
                vendorToUpdate.setCategory(vendor.getCategory());
                vendorToUpdate.setContactPerson(vendor.getContactPerson());
                vendorToUpdate.setEmail(vendor.getEmail());
                vendorToUpdate.setPhone(vendor.getPhone());
                vendorToUpdate.setMobile(vendor.getMobile());
                vendorToUpdate.setAddress(vendor.getAddress());
                vendorToUpdate.setCity(vendor.getCity());
                vendorToUpdate.setState(vendor.getState());
                vendorToUpdate.setCountry(vendor.getCountry());
                vendorToUpdate.setPostalCode(vendor.getPostalCode());
                vendorToUpdate.setGstNumber(vendor.getGstNumber());
                vendorToUpdate.setPanNumber(vendor.getPanNumber());
                vendorToUpdate.setBankName(vendor.getBankName());
                vendorToUpdate.setBankAccountNumber(vendor.getBankAccountNumber());
                vendorToUpdate.setIfscCode(vendor.getIfscCode());
                vendorToUpdate.setPaymentTerms(vendor.getPaymentTerms());
                vendorToUpdate.setCreditLimit(vendor.getCreditLimit());
                vendorToUpdate.setStatus(vendor.getStatus());
                vendorToUpdate.setRating(vendor.getRating());
                vendorToUpdate.setContractStartDate(vendor.getContractStartDate());
                vendorToUpdate.setContractEndDate(vendor.getContractEndDate());
                vendorToUpdate.setEmergencyContact(vendor.getEmergencyContact());
                vendorToUpdate.setServiceAreas(vendor.getServiceAreas());
                vendorToUpdate.setRemarks(vendor.getRemarks());
                vendorToUpdate.setUserId(vendor.getUserId());

                Vendor updatedVendor = vendorRepository.save(vendorToUpdate);

                response.put("status", "success");
                response.put("message", "Vendor updated successfully");
                response.put("data", updatedVendor);
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "Vendor not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to update vendor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Delete vendor
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteVendor(@PathVariable Long id) {
        try {
            Optional<Vendor> vendor = vendorRepository.findById(id);
            Map<String, Object> response = new HashMap<>();

            if (vendor.isPresent()) {
                vendorRepository.deleteById(id);
                response.put("status", "success");
                response.put("message", "Vendor deleted successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "Vendor not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to delete vendor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get vendors by category
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<Map<String, Object>> getVendorsByCategory(@PathVariable String category) {
        try {
            List<Vendor> vendors = vendorRepository.findByCategory(category);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("count", vendors.size());
            response.put("data", vendors);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch vendors by category: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get vendors by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<Map<String, Object>> getVendorsByStatus(@PathVariable String status) {
        try {
            List<Vendor> vendors = vendorRepository.findByStatus(status);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("count", vendors.size());
            response.put("data", vendors);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch vendors by status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get vendors by vendor type
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<Map<String, Object>> getVendorsByType(@PathVariable String type) {
        try {
            List<Vendor> vendors = vendorRepository.findByVendorType(type);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("count", vendors.size());
            response.put("data", vendors);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch vendors by type: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get vendors with expiring contracts
     */
    @GetMapping("/expiring-contracts")
    public ResponseEntity<Map<String, Object>> getExpiringContracts(@RequestParam(defaultValue = "30") int days) {
        try {
            LocalDate cutoffDate = LocalDate.now().plusDays(days);
            List<Vendor> vendors = vendorRepository.findExpiringContracts(cutoffDate);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("count", vendors.size());
            response.put("data", vendors);
            response.put("cutoffDate", cutoffDate);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch expiring contracts: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get top rated vendors
     */
    @GetMapping("/top-rated")
    public ResponseEntity<Map<String, Object>> getTopRatedVendors(@RequestParam(defaultValue = "4") int minRating) {
        try {
            List<Vendor> vendors = vendorRepository.findByMinimumRating(minRating);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("count", vendors.size());
            response.put("data", vendors);
            response.put("minRating", minRating);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch top rated vendors: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Search vendors
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchVendors(@RequestParam String query) {
        try {
            List<Vendor> vendors = vendorRepository.searchVendors(query);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("count", vendors.size());
            response.put("data", vendors);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to search vendors: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get vendor statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getVendorStatistics() {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("total", vendorRepository.count());
            stats.put("active", vendorRepository.countByStatus("ACTIVE"));
            stats.put("inactive", vendorRepository.countByStatus("INACTIVE"));
            stats.put("blacklisted", vendorRepository.countByStatus("BLACKLISTED"));
            stats.put("suspended", vendorRepository.countByStatus("SUSPENDED"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("data", stats);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch vendor statistics: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
