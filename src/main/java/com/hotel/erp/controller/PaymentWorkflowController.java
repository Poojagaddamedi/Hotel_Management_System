package com.hotel.erp.controller;

import com.hotel.erp.entity.Advances;
import com.hotel.erp.service.AdvancesService;
import com.hotel.erp.service.ReservationService;
import com.hotel.erp.service.CheckinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment-workflow")
@CrossOrigin(origins = "*")
public class PaymentWorkflowController {

    @Autowired
    private AdvancesService advancesService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private CheckinService checkinService;

    /**
     * Scenario 1: Pre-checkin Advance Payment
     * Creates advance payment with reservation number only (guest not checked in
     * yet)
     */
    @PostMapping("/pre-checkin-advance")
    public ResponseEntity<Map<String, Object>> createPreCheckinAdvance(@RequestBody Map<String, Object> request) {
        try {
            // Extract request data
            String reservationNo = (String) request.get("reservationNo");
            String guestName = (String) request.get("guestName");
            String paymentMode = (String) request.get("paymentMode");
            Double amount = Double.parseDouble(request.get("amount").toString());
            String remarks = (String) request.get("remarks");
            Integer userId = Integer.parseInt(request.get("userId").toString());

            // Create advance entity
            Advances advance = new Advances();
            advance.setReservationNo(reservationNo);
            advance.setGuestName(guestName);
            advance.setPaymentMode(paymentMode);
            advance.setAmount(BigDecimal.valueOf(amount));
            advance.setRemarks(remarks);
            advance.setUserId(userId);
            advance.setPaymentDate(LocalDateTime.now().toLocalDate());

            // Save advance
            Advances savedAdvance = advancesService.saveAdvance(advance);

            // Prepare response
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Pre-checkin advance payment recorded successfully");
            response.put("advanceId", savedAdvance.getAdvancesId());
            response.put("reservationNo", reservationNo);
            response.put("amount", amount);
            response.put("paymentMode", paymentMode);
            response.put("timestamp", LocalDateTime.now());
            response.put("scenario", "PRE_CHECKIN");
            response.put("description", "Advance payment for reservation before check-in");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to process pre-checkin advance payment");
            errorResponse.put("error", e.getMessage());
            errorResponse.put("scenario", "PRE_CHECKIN");

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Scenario 2: Post-checkin Advance Payment
     * Creates advance payment with folio number + reservation number (guest already
     * checked in)
     */
    @PostMapping("/post-checkin-advance")
    public ResponseEntity<Map<String, Object>> createPostCheckinAdvance(@RequestBody Map<String, Object> request) {
        try {
            // Extract request data
            String folioNo = (String) request.get("folioNo");
            String paymentMode = (String) request.get("paymentMode");
            Double amount = Double.parseDouble(request.get("amount").toString());
            String remarks = (String) request.get("remarks");
            Integer userId = Integer.parseInt(request.get("userId").toString());

            // Create advance entity
            Advances advance = new Advances();
            advance.setFolioNo(folioNo);
            advance.setPaymentMode(paymentMode);
            advance.setAmount(BigDecimal.valueOf(amount));
            advance.setRemarks(remarks);
            advance.setUserId(userId);
            advance.setPaymentDate(LocalDateTime.now().toLocalDate());

            // Try to get guest name from checkin data
            try {
                // For now, set a default guest name - we'll implement proper lookup later
                advance.setGuestName("Guest (Folio: " + folioNo + ")");
            } catch (Exception e) {
                advance.setGuestName("Guest (Folio: " + folioNo + ")");
            }

            // Save advance
            Advances savedAdvance = advancesService.saveAdvance(advance);

            // Prepare response
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Post-checkin advance payment recorded successfully");
            response.put("advanceId", savedAdvance.getAdvancesId());
            response.put("folioNo", folioNo);
            response.put("amount", amount);
            response.put("paymentMode", paymentMode);
            response.put("guestName", advance.getGuestName());
            response.put("timestamp", LocalDateTime.now());
            response.put("scenario", "POST_CHECKIN");
            response.put("description", "Advance payment for checked-in guest with folio");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to process post-checkin advance payment");
            errorResponse.put("error", e.getMessage());
            errorResponse.put("scenario", "POST_CHECKIN");

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Scenario 3: Walk-in Advance Payment
     * Creates advance payment with folio number only (no reservation, direct
     * walk-in)
     */
    @PostMapping("/walk-in-advance")
    public ResponseEntity<Map<String, Object>> createWalkInAdvance(@RequestBody Map<String, Object> request) {
        try {
            // Extract request data
            String guestName = (String) request.get("guestName");
            String paymentMode = (String) request.get("paymentMode");
            Double amount = Double.parseDouble(request.get("amount").toString());
            String remarks = (String) request.get("remarks");
            Integer userId = Integer.parseInt(request.get("userId").toString());
            String folioNo = (String) request.get("folioNo");

            // Generate folio number if not provided
            if (folioNo == null || folioNo.isEmpty()) {
                folioNo = "WI" + System.currentTimeMillis();
            }

            // Create advance entity
            Advances advance = new Advances();
            advance.setFolioNo(folioNo);
            advance.setGuestName(guestName);
            advance.setPaymentMode(paymentMode);
            advance.setAmount(BigDecimal.valueOf(amount));
            advance.setRemarks(remarks);
            advance.setUserId(userId);
            advance.setPaymentDate(LocalDateTime.now().toLocalDate());

            // Save advance
            Advances savedAdvance = advancesService.saveAdvance(advance);

            // Prepare response
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Walk-in advance payment recorded successfully");
            response.put("advanceId", savedAdvance.getAdvancesId());
            response.put("folioNo", folioNo);
            response.put("guestName", guestName);
            response.put("amount", amount);
            response.put("paymentMode", paymentMode);
            response.put("timestamp", LocalDateTime.now());
            response.put("scenario", "WALK_IN");
            response.put("description", "Advance payment for walk-in guest without reservation");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to process walk-in advance payment");
            errorResponse.put("error", e.getMessage());
            errorResponse.put("scenario", "WALK_IN");

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Get payment summary for any identifier (folio, reservation, or guest)
     */
    @GetMapping("/payment-summary/{identifier}")
    public ResponseEntity<Map<String, Object>> getPaymentSummary(
            @PathVariable String identifier,
            @RequestParam(defaultValue = "folio") String type) {

        try {
            Map<String, Object> summary = new HashMap<>();
            summary.put("identifier", identifier);
            summary.put("type", type);
            summary.put("timestamp", LocalDateTime.now());

            // Get payments based on type
            switch (type.toLowerCase()) {
                case "folio":
                    summary.put("payments", advancesService.getAdvancesByFolioNo(identifier));
                    break;
                case "reservation":
                    summary.put("payments", advancesService.getAdvancesByReservationNo(identifier));
                    break;
                case "guest":
                    summary.put("payments", advancesService.getAdvancesByGuestName(identifier));
                    break;
                default:
                    summary.put("payments", advancesService.getAdvancesByFolioNo(identifier));
            }

            summary.put("success", true);
            summary.put("message", "Payment summary retrieved successfully");

            return ResponseEntity.ok(summary);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to retrieve payment summary");
            errorResponse.put("error", e.getMessage());
            errorResponse.put("identifier", identifier);
            errorResponse.put("type", type);

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Get all payment scenarios and their descriptions
     */
    @GetMapping("/scenarios")
    public ResponseEntity<Map<String, Object>> getPaymentScenarios() {
        Map<String, Object> scenarios = new HashMap<>();

        Map<String, Object> preCheckin = new HashMap<>();
        preCheckin.put("name", "Pre-checkin Advance");
        preCheckin.put("description", "Advance payment for reservation before guest check-in");
        preCheckin.put("endpoint", "/api/payment-workflow/pre-checkin-advance");
        preCheckin.put("method", "POST");
        preCheckin.put("requiredFields",
                new String[] { "reservationNo", "guestName", "paymentMode", "amount", "userId" });
        preCheckin.put("linkedTo", "Reservation Number only");

        Map<String, Object> postCheckin = new HashMap<>();
        postCheckin.put("name", "Post-checkin Advance");
        postCheckin.put("description", "Advance payment for guest after check-in with folio");
        postCheckin.put("endpoint", "/api/payment-workflow/post-checkin-advance");
        postCheckin.put("method", "POST");
        postCheckin.put("requiredFields", new String[] { "folioNo", "paymentMode", "amount", "userId" });
        postCheckin.put("linkedTo", "Folio Number + Reservation Number");

        Map<String, Object> walkIn = new HashMap<>();
        walkIn.put("name", "Walk-in Advance");
        walkIn.put("description", "Advance payment for walk-in guest without reservation");
        walkIn.put("endpoint", "/api/payment-workflow/walk-in-advance");
        walkIn.put("method", "POST");
        walkIn.put("requiredFields", new String[] { "guestName", "paymentMode", "amount", "userId" });
        walkIn.put("linkedTo", "Folio Number only (no reservation)");

        scenarios.put("preCheckin", preCheckin);
        scenarios.put("postCheckin", postCheckin);
        scenarios.put("walkIn", walkIn);
        scenarios.put("timestamp", LocalDateTime.now());
        scenarios.put("totalScenarios", 3);

        return ResponseEntity.ok(scenarios);
    }
}
