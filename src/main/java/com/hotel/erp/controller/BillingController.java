package com.hotel.erp.controller;

import com.hotel.erp.dto.BillSettlementDTO;
import com.hotel.erp.dto.FoBillDTO;
import com.hotel.erp.entity.BillSettlement;
import com.hotel.erp.entity.FoBill;
import com.hotel.erp.service.BillingService;
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
@RequestMapping("/api/billing")
@CrossOrigin(origins = "*")
public class BillingController {

    private static final Logger logger = LoggerFactory.getLogger(BillingController.class);

    @Autowired
    private BillingService billingService;

    /**
     * Create a new bill
     */
    @PostMapping("/bills")
    public ResponseEntity<?> createBill(@RequestBody FoBillDTO billDTO) {
        try {
            logger.info("Creating bill for folio: {}", billDTO.getFolioNo());
            FoBill bill = billingService.createBill(billDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(bill);
        } catch (Exception e) {
            logger.error("Error creating bill: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error creating bill: " + e.getMessage());
        }
    }

    /**
     * Create a new bill settlement
     */
    @PostMapping("/settlements")
    public ResponseEntity<?> createSettlement(@RequestBody BillSettlementDTO settlementDTO) {
        try {
            logger.info("Creating settlement for bill: {}", settlementDTO.getBillId());
            BillSettlement settlement = billingService.createSettlement(settlementDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(settlement);
        } catch (Exception e) {
            logger.error("Error creating settlement: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error creating settlement: " + e.getMessage());
        }
    }

    /**
     * Get bills by folio number
     */
    @GetMapping("/bills/folio/{folioNo}")
    public ResponseEntity<List<FoBill>> getBillsByFolio(@PathVariable String folioNo) {
        try {
            List<FoBill> bills = billingService.getBillsByFolio(folioNo);
            return ResponseEntity.ok(bills);
        } catch (Exception e) {
            logger.error("Error retrieving bills: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get settlements by bill ID
     */
    @GetMapping("/settlements/bill/{billId}")
    public ResponseEntity<List<BillSettlement>> getSettlementsByBill(@PathVariable Long billId) {
        try {
            List<BillSettlement> settlements = billingService.getSettlementsByBill(billId);
            return ResponseEntity.ok(settlements);
        } catch (Exception e) {
            logger.error("Error retrieving settlements: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Calculate bill total for a folio
     */
    @GetMapping("/calculate/{folioNo}")
    public ResponseEntity<BigDecimal> calculateBillTotal(@PathVariable String folioNo) {
        try {
            BigDecimal total = billingService.calculateBillTotal(folioNo);
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            logger.error("Error calculating bill total: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get bill summary for a folio
     */
    @GetMapping("/summary/{folioNo}")
    public ResponseEntity<Map<String, Object>> getBillSummary(@PathVariable String folioNo) {
        try {
            Map<String, Object> summary = billingService.getBillSummary(folioNo);
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            logger.error("Error getting bill summary: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get all bills
     */
    @GetMapping("/bills")
    public ResponseEntity<List<FoBill>> getAllBills() {
        try {
            List<FoBill> bills = billingService.getAllBills();
            return ResponseEntity.ok(bills);
        } catch (Exception e) {
            logger.error("Error retrieving all bills: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get all settlements
     */
    @GetMapping("/settlements")
    public ResponseEntity<List<BillSettlement>> getAllSettlements() {
        try {
            List<BillSettlement> settlements = billingService.getAllSettlements();
            return ResponseEntity.ok(settlements);
        } catch (Exception e) {
            logger.error("Error retrieving all settlements: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update a bill
     */
    @PutMapping("/bills/{billId}")
    public ResponseEntity<?> updateBill(@PathVariable Long billId, @RequestBody FoBillDTO billDTO) {
        try {
            FoBill updatedBill = billingService.updateBill(billId, billDTO);
            return ResponseEntity.ok(updatedBill);
        } catch (Exception e) {
            logger.error("Error updating bill: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error updating bill: " + e.getMessage());
        }
    }

    /**
     * Update a settlement
     */
    @PutMapping("/settlements/{settlementId}")
    public ResponseEntity<?> updateSettlement(@PathVariable Long settlementId,
            @RequestBody BillSettlementDTO settlementDTO) {
        try {
            BillSettlement updatedSettlement = billingService.updateSettlement(settlementId, settlementDTO);
            return ResponseEntity.ok(updatedSettlement);
        } catch (Exception e) {
            logger.error("Error updating settlement: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error updating settlement: " + e.getMessage());
        }
    }

    /**
     * Delete a bill
     */
    @DeleteMapping("/bills/{billId}")
    public ResponseEntity<?> deleteBill(@PathVariable Long billId) {
        try {
            billingService.deleteBill(billId);
            return ResponseEntity.ok().body("Bill deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting bill: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error deleting bill: " + e.getMessage());
        }
    }

    /**
     * Delete a settlement
     */
    @DeleteMapping("/settlements/{settlementId}")
    public ResponseEntity<?> deleteSettlement(@PathVariable Long settlementId) {
        try {
            billingService.deleteSettlement(settlementId);
            return ResponseEntity.ok().body("Settlement deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting settlement: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error deleting settlement: " + e.getMessage());
        }
    }

    /**
     * Health check endpoint to test if controller is loaded
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Billing Controller is working!");
    }

    /**
     * Simple test endpoint without service dependency
     */
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Billing Controller test successful - no service dependency");
    }
}