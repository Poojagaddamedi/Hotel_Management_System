package com.hotel.erp.controller;

import com.hotel.erp.entity.Advances;
import com.hotel.erp.entity.PostTransaction;
import com.hotel.erp.entity.FoBill;
import com.hotel.erp.entity.Checkin;
import com.hotel.erp.service.AdvancesService;
import com.hotel.erp.service.PostTransactionService;
import com.hotel.erp.service.BillingService;
import com.hotel.erp.service.CheckinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/billing/complete")
@CrossOrigin(origins = "*")
public class CompleteBillingController {

    private static final Logger logger = LoggerFactory.getLogger(CompleteBillingController.class);

    @Autowired
    private AdvancesService advancesService;

    @Autowired
    private PostTransactionService postTransactionService;

    @Autowired
    private BillingService billingService;

    @Autowired
    private CheckinService checkinService;

    /**
     * Generate Complete Bill Summary with Individual Payment Tracking
     * This includes: Room charges + Service charges - Advance payments = Final
     * amount
     */
    @GetMapping("/summary/{folioNo}")
    public ResponseEntity<?> generateCompleteBillSummary(@PathVariable String folioNo) {
        try {
            logger.info("Generating complete bill summary for folio: {}", folioNo);

            // 1. Get Guest and Reservation Details
            Optional<Checkin> checkinOpt = checkinService.getCheckinByFolioNo(folioNo);
            String guestName = checkinOpt.map(Checkin::getGuestName).orElse("Unknown Guest");
            String reservationNo = checkinOpt.map(Checkin::getReservationNo).orElse(null);
            String roomNo = checkinOpt.map(Checkin::getRoomNo).orElse("Unknown Room");

            // 2. Get All Advance Payments (Individual Tracking)
            List<Advances> allAdvances = advancesService.getAdvancesByFolioNo(folioNo);
            double totalAdvances = allAdvances.stream()
                    .mapToDouble(advance -> advance.getAmount().doubleValue())
                    .sum();

            // 3. Get All Post Transactions (Room charges, service charges, etc.)
            List<PostTransaction> allTransactions = postTransactionService.getTransactionsByFolioNo(folioNo);
            double totalCharges = allTransactions.stream()
                    .mapToDouble(transaction -> transaction.getAmount().doubleValue())
                    .sum(); // 4. Calculate Final Bill Amount
            double grossAmount = totalCharges;
            double netAmount = grossAmount - totalAdvances;
            double balanceAmount = Math.max(0, netAmount); // Cannot be negative
            double excessAdvance = netAmount < 0 ? Math.abs(netAmount) : 0;

            // 5. Group Transactions by Type
            Map<String, Double> chargesByType = new HashMap<>();
            Map<String, List<PostTransaction>> transactionsByType = new HashMap<>();

            for (PostTransaction transaction : allTransactions) {
                String accHead = transaction.getAccHead();
                chargesByType.merge(accHead, transaction.getAmount().doubleValue(), Double::sum);
                transactionsByType.computeIfAbsent(accHead, k -> new ArrayList<>()).add(transaction);
            }

            // 6. Group Advances by Payment Mode
            Map<String, Double> advancesByMode = new HashMap<>();
            Map<String, List<Advances>> advancesByType = new HashMap<>();

            for (Advances advance : allAdvances) {
                String paymentMode = advance.getPaymentMode();
                advancesByMode.merge(paymentMode, advance.getAmount().doubleValue(), Double::sum); // Determine advance
                                                                                                   // type
                String advanceType = determineAdvanceType(advance);
                advancesByType.computeIfAbsent(advanceType, k -> new ArrayList<>()).add(advance);
            }

            // 7. Create Complete Bill Summary
            Map<String, Object> billSummary = new HashMap<>();

            // Guest Information
            billSummary.put("guestDetails", Map.of(
                    "guestName", guestName,
                    "folioNo", folioNo,
                    "reservationNo", reservationNo != null ? reservationNo : "Walk-in",
                    "roomNo", roomNo));

            // Charges Breakdown
            billSummary.put("chargesBreakdown", Map.of(
                    "totalCharges", totalCharges,
                    "chargesByType", chargesByType,
                    "transactionsByType", transactionsByType,
                    "transactionCount", allTransactions.size()));

            // Advances Breakdown
            billSummary.put("advancesBreakdown", Map.of(
                    "totalAdvances", totalAdvances,
                    "advancesByMode", advancesByMode,
                    "advancesByType", advancesByType,
                    "advanceCount", allAdvances.size()));

            // Final Calculation
            billSummary.put("billCalculation", Map.of(
                    "grossAmount", grossAmount,
                    "totalAdvances", totalAdvances,
                    "netAmount", netAmount,
                    "balanceAmount", balanceAmount,
                    "excessAdvance", excessAdvance,
                    "status", balanceAmount > 0 ? "PAYMENT_DUE" : (excessAdvance > 0 ? "REFUND_DUE" : "SETTLED")));

            // Individual Payment Tracking
            billSummary.put("individualTracking", Map.of(
                    "allAdvances", allAdvances,
                    "allTransactions", allTransactions));

            // Metadata
            billSummary.put("metadata", Map.of(
                    "generatedOn", LocalDateTime.now(),
                    "generatedBy", "system",
                    "status", "success"));

            return ResponseEntity.ok(billSummary);

        } catch (Exception e) {
            logger.error("Error generating complete bill summary: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Error generating bill summary: " + e.getMessage()));
        }
    }

    /**
     * Generate Final Bill for Checkout
     */
    @PostMapping("/generate-final-bill/{folioNo}")
    public ResponseEntity<?> generateFinalBill(@PathVariable String folioNo,
            @RequestBody Map<String, Object> billData) {
        try {
            logger.info("Generating final bill for checkout: {}", folioNo);

            // Get complete bill summary first
            ResponseEntity<?> summaryResponse = generateCompleteBillSummary(folioNo);
            if (summaryResponse.getStatusCode().value() != 200) {
                return summaryResponse;
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> summary = (Map<String, Object>) summaryResponse.getBody();

            @SuppressWarnings("unchecked")
            Map<String, Object> calculation = (Map<String, Object>) summary.get("billCalculation");

            @SuppressWarnings("unchecked")
            Map<String, Object> guestDetails = (Map<String, Object>) summary.get("guestDetails");

            // Create final bill
            Map<String, Object> finalBill = new HashMap<>();
            finalBill.put("folioNo", folioNo);
            finalBill.put("billDate", LocalDateTime.now());
            finalBill.put("totalAmount", calculation.get("grossAmount"));
            finalBill.put("advanceAmount", calculation.get("totalAdvances"));
            finalBill.put("balanceAmount", calculation.get("balanceAmount"));
            finalBill.put("guestName", guestDetails.get("guestName"));
            finalBill.put("roomNo", guestDetails.get("roomNo"));
            finalBill.put("userId", billData.get("userId"));

            // Save final bill to database
            FoBill bill = new FoBill();
            bill.setFolioNo(folioNo);
            bill.setBillDate(LocalDateTime.now().toLocalDate());
            bill.setTotalAmount(BigDecimal.valueOf((Double) calculation.get("grossAmount")));
            bill.setUserId((Integer) billData.get("userId"));

            FoBill createdBill = billingService.saveBill(bill);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Final bill generated successfully");
            response.put("bill", createdBill);
            response.put("summary", summary);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error generating final bill: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Error generating final bill: " + e.getMessage()));
        }
    }

    /**
     * Get Complete Guest Journey (All payments and charges)
     */
    @GetMapping("/guest-journey/{identifier}")
    public ResponseEntity<?> getCompleteGuestJourney(@PathVariable String identifier,
            @RequestParam String type) {
        try {
            logger.info("Getting complete guest journey for {}: {}", type, identifier);

            List<Advances> advances = new ArrayList<>();
            List<PostTransaction> transactions = new ArrayList<>();
            String guestName = "";
            String folioNo = "";
            String reservationNo = "";

            // Get data based on identifier type
            switch (type.toLowerCase()) {
                case "folio":
                    advances = advancesService.getAdvancesByFolioNo(identifier);
                    transactions = postTransactionService.getTransactionsByFolioNo(identifier);
                    Optional<Checkin> checkinOpt = checkinService.getCheckinByFolioNo(identifier);
                    guestName = checkinOpt.map(Checkin::getGuestName).orElse("Unknown Guest");
                    folioNo = identifier;
                    reservationNo = checkinOpt.map(Checkin::getReservationNo).orElse(null);
                    break;
                case "reservation":
                    advances = advancesService.getAdvancesByReservationNo(identifier);
                    transactions = postTransactionService.getTransactionsByReservationNo(identifier);
                    reservationNo = identifier;
                    // Get folio from checkin if exists
                    Optional<Checkin> checkinByRes = checkinService.getCheckinByReservationNo(identifier);
                    folioNo = checkinByRes.map(Checkin::getFolioNo).orElse(null);
                    if (folioNo != null) {
                        guestName = checkinByRes.map(Checkin::getGuestName).orElse("Unknown Guest");
                    }
                    break;
                case "guest":
                    // Note: This would require implementing search by guest name in repositories
                    guestName = identifier;
                    break;
                default:
                    return ResponseEntity.badRequest().body(Map.of(
                            "status", "error",
                            "message", "Invalid type. Use: folio, reservation, or guest"));
            }

            // Calculate totals
            double totalAdvances = advances.stream().mapToDouble(advance -> advance.getAmount().doubleValue()).sum();
            double totalCharges = transactions.stream()
                    .mapToDouble(transaction -> transaction.getAmount().doubleValue()).sum();

            // Create timeline of all activities
            List<Map<String, Object>> timeline = new ArrayList<>();

            // Add advances to timeline
            for (Advances advance : advances) {
                Map<String, Object> event = new HashMap<>();
                event.put("type", "ADVANCE_PAYMENT");
                event.put("date", advance.getPaymentDate().atStartOfDay());
                event.put("amount", advance.getAmount().doubleValue());
                event.put("paymentMode", advance.getPaymentMode());
                event.put("remarks", advance.getRemarks());
                event.put("id", advance.getAdvancesId());
                timeline.add(event);
            }

            // Add transactions to timeline
            for (PostTransaction transaction : transactions) {
                Map<String, Object> event = new HashMap<>();
                event.put("type", "CHARGE");
                event.put("date", transaction.getTransDate().atStartOfDay());
                event.put("amount", transaction.getAmount().doubleValue());
                event.put("accountHead", transaction.getAccHead());
                event.put("narration", transaction.getNarration());
                event.put("id", transaction.getId());
                timeline.add(event);
            }

            // Sort timeline by date
            timeline.sort((a, b) -> {
                Object dateA = a.get("date");
                Object dateB = b.get("date");
                if (dateA instanceof LocalDateTime && dateB instanceof LocalDateTime) {
                    return ((LocalDateTime) dateA).compareTo((LocalDateTime) dateB);
                }
                return 0;
            });

            Map<String, Object> journey = new HashMap<>();
            journey.put("status", "success");
            journey.put("guestName", guestName);
            journey.put("folioNo", folioNo);
            journey.put("reservationNo", reservationNo);
            journey.put("totalAdvances", totalAdvances);
            journey.put("totalCharges", totalCharges);
            journey.put("netAmount", totalCharges - totalAdvances);
            journey.put("timeline", timeline);
            journey.put("advanceCount", advances.size());
            journey.put("chargeCount", transactions.size());

            return ResponseEntity.ok(journey);

        } catch (Exception e) {
            logger.error("Error getting guest journey: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Error retrieving guest journey: " + e.getMessage()));
        }
    }

    /**
     * Helper method to determine advance type
     */
    private String determineAdvanceType(Advances advance) {
        if (advance.getReservationNo() != null && advance.getFolioNo() == null) {
            return "PRE_CHECKIN";
        } else if (advance.getFolioNo() != null && advance.getReservationNo() != null) {
            return "POST_CHECKIN";
        } else if (advance.getFolioNo() != null && advance.getReservationNo() == null) {
            return "WALK_IN";
        } else {
            return "UNKNOWN";
        }
    }
}
