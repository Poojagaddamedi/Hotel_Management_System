package com.hotel.erp.controller;

import com.hotel.erp.dto.AdditionalChargesDTO;
import com.hotel.erp.entity.AdditionalCharges;
import com.hotel.erp.service.AdditionalChargesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/charges")
@CrossOrigin(origins = "*")
public class AdditionalChargesController {

    private static final Logger logger = LoggerFactory.getLogger(AdditionalChargesController.class);

    @Autowired
    private AdditionalChargesService additionalChargesService;

    /**
     * Create a new additional charge
     */
    @PostMapping
    public ResponseEntity<?> createCharge(@RequestBody AdditionalChargesDTO chargeDTO) {
        try {
            logger.info("Creating additional charge for folio: {}", chargeDTO.getFolioNo());
            AdditionalCharges charge = additionalChargesService.createCharge(chargeDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(charge);
        } catch (Exception e) {
            logger.error("Error creating charge: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error creating charge: " + e.getMessage());
        }
    }

    /**
     * Get charges by folio number
     */
    @GetMapping("/folio/{folioNo}")
    public ResponseEntity<List<AdditionalCharges>> getChargesByFolio(@PathVariable String folioNo) {
        try {
            List<AdditionalCharges> charges = additionalChargesService.getChargesByFolio(folioNo);
            return ResponseEntity.ok(charges);
        } catch (Exception e) {
            logger.error("Error retrieving charges: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get charges by reservation number
     */
    @GetMapping("/reservation/{reservationNo}")
    public ResponseEntity<List<AdditionalCharges>> getChargesByReservation(@PathVariable String reservationNo) {
        try {
            List<AdditionalCharges> charges = additionalChargesService.getChargesByReservation(reservationNo);
            return ResponseEntity.ok(charges);
        } catch (Exception e) {
            logger.error("Error retrieving charges: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get charges by charge type
     */
    @GetMapping("/type/{chargeType}")
    public ResponseEntity<List<AdditionalCharges>> getChargesByType(@PathVariable String chargeType) {
        try {
            List<AdditionalCharges> charges = additionalChargesService.getChargesByType(chargeType);
            return ResponseEntity.ok(charges);
        } catch (Exception e) {
            logger.error("Error retrieving charges: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Calculate total charges for a folio
     */
    @GetMapping("/total/{folioNo}")
    public ResponseEntity<BigDecimal> calculateTotalCharges(@PathVariable String folioNo) {
        try {
            BigDecimal total = additionalChargesService.calculateTotalCharges(folioNo);
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            logger.error("Error calculating total charges: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get charge summary for a folio
     */
    @GetMapping("/summary/{folioNo}")
    public ResponseEntity<Map<String, Object>> getChargeSummary(@PathVariable String folioNo) {
        try {
            Map<String, Object> summary = additionalChargesService.getChargeSummary(folioNo);
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            logger.error("Error getting charge summary: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get all charges
     */
    @GetMapping
    public ResponseEntity<List<AdditionalCharges>> getAllCharges() {
        try {
            List<AdditionalCharges> charges = additionalChargesService.getAllCharges();
            return ResponseEntity.ok(charges);
        } catch (Exception e) {
            logger.error("Error retrieving all charges: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update a charge
     */
    @PutMapping("/{chargeId}")
    public ResponseEntity<?> updateCharge(@PathVariable Long chargeId, @RequestBody AdditionalChargesDTO chargeDTO) {
        try {
            AdditionalCharges updatedCharge = additionalChargesService.updateCharge(chargeId, chargeDTO);
            return ResponseEntity.ok(updatedCharge);
        } catch (Exception e) {
            logger.error("Error updating charge: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error updating charge: " + e.getMessage());
        }
    }

    /**
     * Delete a charge
     */
    @DeleteMapping("/{chargeId}")
    public ResponseEntity<?> deleteCharge(@PathVariable Long chargeId) {
        try {
            additionalChargesService.deleteCharge(chargeId);
            return ResponseEntity.ok().body("Charge deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting charge: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error deleting charge: " + e.getMessage());
        }
    }

    /**
     * Search charges
     */
    @GetMapping("/search")
    public ResponseEntity<List<AdditionalCharges>> searchCharges(@RequestParam String query) {
        try {
            List<AdditionalCharges> charges = additionalChargesService.searchCharges(query);
            return ResponseEntity.ok(charges);
        } catch (Exception e) {
            logger.error("Error searching charges: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get charges by date range
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<AdditionalCharges>> getChargesByDateRange(
            @RequestParam String startDate, 
            @RequestParam String endDate) {
        try {
            List<AdditionalCharges> charges = additionalChargesService.getChargesByDateRange(startDate, endDate);
            return ResponseEntity.ok(charges);
        } catch (Exception e) {
            logger.error("Error retrieving charges by date range: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 