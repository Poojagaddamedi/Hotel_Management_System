package com.hotel.erp.controller;

import com.hotel.erp.entity.Checkin;
import com.hotel.erp.service.CheckoutsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/checkouts")
@CrossOrigin(origins = "*")
public class CheckoutsController {

    private static final Logger logger = LoggerFactory.getLogger(CheckoutsController.class);

    @Autowired
    private CheckoutsService checkoutsService;

    /**
     * Perform checkout for a guest
     */
    @PostMapping("/{folioNo}")
    public ResponseEntity<?> performCheckout(
            @PathVariable String folioNo,
            @RequestParam(required = false) String departureDate,
            @RequestParam(required = false) String remarks,
            @RequestParam(required = false) Integer userId) {
        try {
            logger.info("Performing checkout for folio: {}", folioNo);
            LocalDate departure = departureDate != null ? LocalDate.parse(departureDate) : LocalDate.now();
            Checkin checkout = checkoutsService.performCheckout(folioNo, departure, remarks, userId);
            return ResponseEntity.ok(checkout);
        } catch (Exception e) {
            logger.error("Error performing checkout: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error performing checkout: " + e.getMessage());
        }
    }

    /**
     * Calculate outstanding amount for a folio
     */
    @GetMapping("/outstanding/{folioNo}")
    public ResponseEntity<BigDecimal> calculateOutstandingAmount(@PathVariable String folioNo) {
        try {
            BigDecimal outstanding = checkoutsService.calculateOutstandingAmount(folioNo);
            return ResponseEntity.ok(outstanding);
        } catch (Exception e) {
            logger.error("Error calculating outstanding amount: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get checkout history
     */
    @GetMapping("/history")
    public ResponseEntity<List<Checkin>> getCheckoutHistory() {
        try {
            List<Checkin> checkouts = checkoutsService.getCheckoutHistory();
            return ResponseEntity.ok(checkouts);
        } catch (Exception e) {
            logger.error("Error retrieving checkout history: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get checkout details for a folio
     */
    @GetMapping("/details/{folioNo}")
    public ResponseEntity<Map<String, Object>> getCheckoutDetails(@PathVariable String folioNo) {
        try {
            Map<String, Object> details = checkoutsService.getCheckoutDetails(folioNo);
            return ResponseEntity.ok(details);
        } catch (Exception e) {
            logger.error("Error getting checkout details: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Validate checkout eligibility
     */
    @GetMapping("/validate/{folioNo}")
    public ResponseEntity<Map<String, Object>> validateCheckout(@PathVariable String folioNo) {
        try {
            Map<String, Object> validation = checkoutsService.validateCheckout(folioNo);
            return ResponseEntity.ok(validation);
        } catch (Exception e) {
            logger.error("Error validating checkout: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get checkout summary
     */
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getCheckoutSummary() {
        try {
            Map<String, Object> summary = checkoutsService.getCheckoutSummary();
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            logger.error("Error getting checkout summary: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get checkouts by date range
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<Checkin>> getCheckoutsByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            List<Checkin> checkouts = checkoutsService.getCheckoutsByDateRange(startDate, endDate);
            return ResponseEntity.ok(checkouts);
        } catch (Exception e) {
            logger.error("Error retrieving checkouts by date range: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get today's checkouts
     */
    @GetMapping("/today")
    public ResponseEntity<List<Checkin>> getTodayCheckouts() {
        try {
            List<Checkin> checkouts = checkoutsService.getTodayCheckouts();
            return ResponseEntity.ok(checkouts);
        } catch (Exception e) {
            logger.error("Error retrieving today's checkouts: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get overdue checkouts
     */
    @GetMapping("/overdue")
    public ResponseEntity<List<Checkin>> getOverdueCheckouts() {
        try {
            List<Checkin> checkouts = checkoutsService.getOverdueCheckouts();
            return ResponseEntity.ok(checkouts);
        } catch (Exception e) {
            logger.error("Error retrieving overdue checkouts: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Process payment during checkout
     */
    @PostMapping("/{folioNo}/payment")
    public ResponseEntity<?> processPayment(
            @PathVariable String folioNo,
            @RequestParam BigDecimal amount,
            @RequestParam String paymentMode,
            @RequestParam(required = false) String referenceNo,
            @RequestParam(required = false) String remarks) {
        try {
            logger.info("Processing payment for folio: {}", folioNo);
            Map<String, Object> result = checkoutsService.processPayment(folioNo, amount, paymentMode, referenceNo,
                    remarks);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error processing payment: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error processing payment: " + e.getMessage());
        }
    }

    /**
     * Generate checkout receipt
     */
    @GetMapping("/{folioNo}/receipt")
    public ResponseEntity<Map<String, Object>> generateReceipt(@PathVariable String folioNo) {
        try {
            Map<String, Object> receipt = checkoutsService.generateReceipt(folioNo);
            return ResponseEntity.ok(receipt);
        } catch (Exception e) {
            logger.error("Error generating receipt: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Cancel checkout
     */
    @PostMapping("/{folioNo}/cancel")
    public ResponseEntity<?> cancelCheckout(@PathVariable String folioNo) {
        try {
            logger.info("Cancelling checkout for folio: {}", folioNo);
            Map<String, Object> result = checkoutsService.cancelCheckout(folioNo);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error cancelling checkout: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error cancelling checkout: " + e.getMessage());
        }
    }

    /**
     * Health check endpoint to test if controller is loaded
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Checkouts Controller is working!");
    }

    /**
     * Simple test endpoint without service dependency
     */
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Checkouts Controller test successful - no service dependency");
    }
}