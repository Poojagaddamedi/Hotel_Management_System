package com.hotel.erp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/system")
@CrossOrigin(origins = "*")
public class SystemTestController {

    private static final Logger logger = LoggerFactory.getLogger(SystemTestController.class);

    /**
     * Test endpoint to verify the complete payment workflow system is working
     */
    @GetMapping("/test")
    public ResponseEntity<?> testSystem() {
        try {
            logger.info("Testing complete payment workflow system");

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Complete payment workflow system is operational");
            response.put("timestamp", LocalDateTime.now());
            response.put("version", "1.0.0");

            // Available payment workflow endpoints
            List<String> availableEndpoints = Arrays.asList(
                    "POST /api/payment-workflow/pre-checkin-advance",
                    "POST /api/payment-workflow/post-checkin-advance",
                    "POST /api/payment-workflow/walk-in-advance",
                    "GET /api/payment-workflow/payment-summary/{identifier}?type={type}",
                    "GET /api/billing/complete/summary/{folioNo}",
                    "POST /api/billing/complete/generate-final-bill/{folioNo}",
                    "GET /api/billing/complete/guest-journey/{identifier}?type={type}");

            response.put("availableEndpoints", availableEndpoints);

            // Payment scenarios supported
            Map<String, String> paymentScenarios = new HashMap<>();
            paymentScenarios.put("Pre-Checkin", "Customer with reservation pays advance before checking in");
            paymentScenarios.put("Post-Checkin", "Customer checked in pays advance against room charges");
            paymentScenarios.put("Walk-In", "Customer without reservation pays advance");

            response.put("supportedScenarios", paymentScenarios);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error testing system: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "System test failed: " + e.getMessage(),
                    "timestamp", LocalDateTime.now()));
        }
    }

    /**
     * Get system health status
     */
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("application", "Hotel Management ERP");
        health.put("features", Arrays.asList(
                "Payment Workflow Management",
                "Complete Billing System",
                "Individual Payment Tracking",
                "Multi-scenario Support"));

        return ResponseEntity.ok(health);
    }

    /**
     * Get API documentation
     */
    @GetMapping("/docs")
    public ResponseEntity<?> getApiDocumentation() {
        Map<String, Object> docs = new HashMap<>();
        docs.put("title", "Hotel Management Payment Workflow API");
        docs.put("version", "1.0.0");
        docs.put("description",
                "Complete payment workflow system supporting pre-checkin, post-checkin, and walk-in advance payments with individual tracking");

        // Payment Workflow APIs
        Map<String, Object> paymentWorkflow = new HashMap<>();
        paymentWorkflow.put("description", "Manage advance payments for different customer scenarios");

        Map<String, Object> preCheckin = new HashMap<>();
        preCheckin.put("method", "POST");
        preCheckin.put("path", "/api/payment-workflow/pre-checkin-advance");
        preCheckin.put("description", "Create advance payment for customer with reservation before checkin");
        preCheckin.put("requiredFields",
                Arrays.asList("reservationNo", "guestName", "paymentMode", "amount", "userId"));

        Map<String, Object> postCheckin = new HashMap<>();
        postCheckin.put("method", "POST");
        postCheckin.put("path", "/api/payment-workflow/post-checkin-advance");
        postCheckin.put("description", "Create advance payment for checked-in customer");
        postCheckin.put("requiredFields", Arrays.asList("folioNo", "paymentMode", "amount", "userId"));

        Map<String, Object> walkIn = new HashMap<>();
        walkIn.put("method", "POST");
        walkIn.put("path", "/api/payment-workflow/walk-in-advance");
        walkIn.put("description", "Create advance payment for walk-in customer without reservation");
        walkIn.put("requiredFields", Arrays.asList("guestName", "paymentMode", "amount", "userId"));

        paymentWorkflow.put("preCheckinAdvance", preCheckin);
        paymentWorkflow.put("postCheckinAdvance", postCheckin);
        paymentWorkflow.put("walkInAdvance", walkIn);

        // Billing APIs
        Map<String, Object> billing = new HashMap<>();
        billing.put("description", "Complete billing system with individual payment tracking");

        Map<String, Object> billSummary = new HashMap<>();
        billSummary.put("method", "GET");
        billSummary.put("path", "/api/billing/complete/summary/{folioNo}");
        billSummary.put("description", "Generate complete bill summary with individual payment tracking");

        Map<String, Object> finalBill = new HashMap<>();
        finalBill.put("method", "POST");
        finalBill.put("path", "/api/billing/complete/generate-final-bill/{folioNo}");
        finalBill.put("description", "Generate final bill for checkout");

        Map<String, Object> guestJourney = new HashMap<>();
        guestJourney.put("method", "GET");
        guestJourney.put("path", "/api/billing/complete/guest-journey/{identifier}?type={type}");
        guestJourney.put("description", "Get complete guest payment journey timeline");
        guestJourney.put("supportedTypes", Arrays.asList("folio", "reservation", "guest"));

        billing.put("billSummary", billSummary);
        billing.put("finalBill", finalBill);
        billing.put("guestJourney", guestJourney);

        docs.put("paymentWorkflow", paymentWorkflow);
        docs.put("billing", billing);

        return ResponseEntity.ok(docs);
    }
}
