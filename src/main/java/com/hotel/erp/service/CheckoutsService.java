package com.hotel.erp.service;

import com.hotel.erp.entity.Checkin;
import com.hotel.erp.entity.Advances;
import com.hotel.erp.entity.FoBill;
import com.hotel.erp.entity.AdditionalCharges;
import com.hotel.erp.repository.CheckinRepository;
import com.hotel.erp.repository.AdvancesRepository;
import com.hotel.erp.repository.FoBillRepository;
import com.hotel.erp.repository.AdditionalChargesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class CheckoutsService {
    
    private static final Logger logger = LoggerFactory.getLogger(CheckoutsService.class);

    @Autowired
    private CheckinRepository checkinRepository;

    @Autowired
    private AdvancesRepository advancesRepository;

    @Autowired
    private FoBillRepository foBillRepository;

    @Autowired
    private AdditionalChargesRepository additionalChargesRepository;

    /**
     * Perform check-out for a guest
     * 
     * @param folioNo the folio number
     * @param departureDate the departure date
     * @param remarks the check-out remarks
     * @param userId the user ID performing the check-out
     * @return the updated checkin record
     */
    public Checkin performCheckout(String folioNo, LocalDate departureDate, String remarks, Integer userId) {
        logger.info("Performing check-out for folio: {}", folioNo);
        
        try {
            // Find the check-in record
            Optional<Checkin> checkinOpt = checkinRepository.findByFolioNo(folioNo);
            if (checkinOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Check-in not found for folio: " + folioNo);
            }
            
            Checkin checkin = checkinOpt.get();
            
            // Validate departure date
            if (departureDate.isBefore(checkin.getCheckInDate())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Departure date cannot be before check-in date");
            }
            
            // Check if already checked out
            if (checkin.getCheckOutDate() != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Guest already checked out");
            }
            
            // Calculate outstanding amount
            BigDecimal outstandingAmount = calculateOutstandingAmount(folioNo);
            
            // Update check-in record
            checkin.setCheckOutDate(departureDate);
            checkin.setRemarks(remarks);
            checkin.setUpdatedOn(LocalDateTime.now());
            checkin.setUserId(userId);
            
            Checkin savedCheckin = checkinRepository.save(checkin);
            
            logger.info("Check-out completed for folio: {} with outstanding amount: {}", folioNo, outstandingAmount);
            return savedCheckin;
            
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error performing check-out: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error performing check-out");
        }
    }

    /**
     * Calculate outstanding amount for a folio
     * 
     * @param folioNo the folio number
     * @return the outstanding amount
     */
    public BigDecimal calculateOutstandingAmount(String folioNo) {
        try {
            // Get total advances
            BigDecimal totalAdvances = advancesRepository.getTotalAmountByFolioNo(folioNo);
            
            // Get total bills
            BigDecimal totalBills = foBillRepository.getTotalAmountByFolioNo(folioNo);
            
            // Get total charges
            BigDecimal totalCharges = additionalChargesRepository.getTotalAmountByFolioNo(folioNo);
            
            // Calculate outstanding
            BigDecimal totalOwed = totalBills.add(totalCharges);
            BigDecimal outstanding = totalOwed.subtract(totalAdvances);
            
            logger.info("Outstanding amount for folio {}: {}", folioNo, outstanding);
            return outstanding;
            
        } catch (Exception e) {
            logger.error("Error calculating outstanding amount: {}", e.getMessage(), e);
            return BigDecimal.ZERO;
        }
    }

    /**
     * Get check-out history
     * 
     * @return List of checked out guests
     */
    public List<Checkin> getCheckoutHistory() {
        try {
            List<Checkin> checkouts = checkinRepository.findByCheckOutDateIsNotNull();
            logger.info("Retrieved {} check-out records", checkouts.size());
            return checkouts;
        } catch (Exception e) {
            logger.error("Error retrieving check-out history: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Get check-out details by folio number
     * 
     * @param folioNo the folio number
     * @return the check-out details
     */
    public Map<String, Object> getCheckoutDetails(String folioNo) {
        logger.info("Getting check-out details for folio: {}", folioNo);
        
        try {
            Optional<Checkin> checkinOpt = checkinRepository.findByFolioNo(folioNo);
            if (checkinOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Check-in not found for folio: " + folioNo);
            }
            
            Checkin checkin = checkinOpt.get();
            Map<String, Object> details = new HashMap<>();
            
            details.put("checkin", checkin);
            details.put("outstandingAmount", calculateOutstandingAmount(folioNo));
            
            // Get payment history
            List<Advances> advances = advancesRepository.findByFolioNo(folioNo);
            details.put("payments", advances);
            
            // Get bill history
            List<FoBill> bills = foBillRepository.findByFolioNo(folioNo);
            details.put("bills", bills);
            
            // Get charge history
            List<AdditionalCharges> charges = additionalChargesRepository.findByFolioNo(folioNo);
            details.put("charges", charges);
            
            logger.info("Retrieved check-out details for folio: {}", folioNo);
            return details;
            
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error getting check-out details: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting check-out details");
        }
    }

    /**
     * Validate check-out eligibility
     * 
     * @param folioNo the folio number
     * @return validation result
     */
    public Map<String, Object> validateCheckout(String folioNo) {
        logger.info("Validating check-out for folio: {}", folioNo);
        
        try {
            Map<String, Object> validation = new HashMap<>();
            
            Optional<Checkin> checkinOpt = checkinRepository.findByFolioNo(folioNo);
            if (checkinOpt.isEmpty()) {
                validation.put("eligible", false);
                validation.put("message", "Check-in not found");
                return validation;
            }
            
            Checkin checkin = checkinOpt.get();
            
            // Check if already checked out
            if (checkin.getCheckOutDate() != null) {
                validation.put("eligible", false);
                validation.put("message", "Already checked out");
                return validation;
            }
            
            // Check outstanding amount
            BigDecimal outstanding = calculateOutstandingAmount(folioNo);
            validation.put("outstandingAmount", outstanding);
            validation.put("eligible", true);
            validation.put("message", "Eligible for check-out");
            
            logger.info("Check-out validation completed for folio: {}", folioNo);
            return validation;
            
        } catch (Exception e) {
            logger.error("Error validating check-out: {}", e.getMessage(), e);
            Map<String, Object> validation = new HashMap<>();
            validation.put("eligible", false);
            validation.put("message", "Error validating check-out");
            return validation;
        }
    }

    /**
     * Get checkout summary
     * 
     * @return checkout summary
     */
    public Map<String, Object> getCheckoutSummary() {
        try {
            Map<String, Object> summary = new HashMap<>();
            
            // Get total checkouts today
            List<Checkin> todayCheckouts = getTodayCheckouts();
            summary.put("todayCheckouts", todayCheckouts.size());
            
            // Get total checkouts this month
            List<Checkin> monthCheckouts = getCheckoutsByDateRange(
                LocalDate.now().withDayOfMonth(1).toString(),
                LocalDate.now().toString()
            );
            summary.put("monthCheckouts", monthCheckouts.size());
            
            // Get overdue checkouts
            List<Checkin> overdueCheckouts = getOverdueCheckouts();
            summary.put("overdueCheckouts", overdueCheckouts.size());
            
            logger.info("Checkout summary generated");
            return summary;
            
        } catch (Exception e) {
            logger.error("Error getting checkout summary: {}", e.getMessage(), e);
            return new HashMap<>();
        }
    }

    /**
     * Get checkouts by date range
     * 
     * @param startDate start date string
     * @param endDate end date string
     * @return list of checkouts in date range
     */
    public List<Checkin> getCheckoutsByDateRange(String startDate, String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            
            List<Checkin> allCheckouts = checkinRepository.findByCheckOutDateIsNotNull();
            List<Checkin> filteredCheckouts = allCheckouts.stream()
                .filter(checkin -> checkin.getCheckOutDate() != null &&
                                 !checkin.getCheckOutDate().isBefore(start) &&
                                 !checkin.getCheckOutDate().isAfter(end))
                .collect(Collectors.toList());
            
            logger.info("Retrieved {} checkouts between {} and {}", filteredCheckouts.size(), startDate, endDate);
            return filteredCheckouts;
            
        } catch (Exception e) {
            logger.error("Error getting checkouts by date range: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Get today's checkouts
     * 
     * @return list of today's checkouts
     */
    public List<Checkin> getTodayCheckouts() {
        try {
            LocalDate today = LocalDate.now();
            List<Checkin> allCheckouts = checkinRepository.findByCheckOutDateIsNotNull();
            List<Checkin> todayCheckouts = allCheckouts.stream()
                .filter(checkin -> checkin.getCheckOutDate() != null &&
                                 checkin.getCheckOutDate().equals(today))
                .collect(Collectors.toList());
            
            logger.info("Retrieved {} checkouts for today", todayCheckouts.size());
            return todayCheckouts;
            
        } catch (Exception e) {
            logger.error("Error getting today's checkouts: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Get overdue checkouts
     * 
     * @return list of overdue checkouts
     */
    public List<Checkin> getOverdueCheckouts() {
        try {
            LocalDate today = LocalDate.now();
            List<Checkin> allCheckouts = checkinRepository.findByCheckOutDateIsNotNull();
            List<Checkin> overdueCheckouts = allCheckouts.stream()
                .filter(checkin -> checkin.getCheckOutDate() != null &&
                                 checkin.getCheckOutDate().isBefore(today))
                .collect(Collectors.toList());
            
            logger.info("Retrieved {} overdue checkouts", overdueCheckouts.size());
            return overdueCheckouts;
            
        } catch (Exception e) {
            logger.error("Error getting overdue checkouts: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Process payment for checkout
     * 
     * @param folioNo folio number
     * @param amount payment amount
     * @param paymentMode payment mode
     * @param referenceNo reference number
     * @param remarks payment remarks
     * @return payment result
     */
    public Map<String, Object> processPayment(String folioNo, BigDecimal amount, String paymentMode, String referenceNo, String remarks) {
        try {
            Map<String, Object> result = new HashMap<>();
            
            // Validate folio exists
            Optional<Checkin> checkinOpt = checkinRepository.findByFolioNo(folioNo);
            if (checkinOpt.isEmpty()) {
                result.put("success", false);
                result.put("message", "Folio not found");
                return result;
            }
            
            // Create advance record for payment
            Advances payment = new Advances();
            payment.setFolioNo(folioNo);
            payment.setAmount(amount);
            payment.setPaymentMode(paymentMode);
            payment.setReferenceNo(referenceNo);
            payment.setRemarks(remarks);
            payment.setPaymentDate(LocalDate.now());
            payment.setAuditDate(LocalDate.now());
            payment.setCreatedOn(LocalDateTime.now());
            payment.setUpdatedOn(LocalDateTime.now());
            
            Advances savedPayment = advancesRepository.save(payment);
            
            result.put("success", true);
            result.put("message", "Payment processed successfully");
            result.put("paymentId", savedPayment.getAdvancesId());
            
            logger.info("Payment processed for folio {}: {}", folioNo, amount);
            return result;
            
        } catch (Exception e) {
            logger.error("Error processing payment: {}", e.getMessage(), e);
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "Error processing payment: " + e.getMessage());
            return result;
        }
    }

    /**
     * Generate receipt for checkout
     * 
     * @param folioNo folio number
     * @return receipt data
     */
    public Map<String, Object> generateReceipt(String folioNo) {
        try {
            Map<String, Object> receipt = new HashMap<>();
            
            // Get checkout details
            Map<String, Object> checkoutDetails = getCheckoutDetails(folioNo);
            receipt.put("checkoutDetails", checkoutDetails);
            
            // Get outstanding amount
            BigDecimal outstanding = calculateOutstandingAmount(folioNo);
            receipt.put("outstandingAmount", outstanding);
            
            // Generate receipt number
            String receiptNo = "RCP" + System.currentTimeMillis();
            receipt.put("receiptNo", receiptNo);
            receipt.put("generatedOn", LocalDateTime.now());
            
            logger.info("Receipt generated for folio: {}", folioNo);
            return receipt;
            
        } catch (Exception e) {
            logger.error("Error generating receipt: {}", e.getMessage(), e);
            return new HashMap<>();
        }
    }

    /**
     * Cancel checkout
     * 
     * @param folioNo folio number
     * @return cancellation result
     */
    public Map<String, Object> cancelCheckout(String folioNo) {
        try {
            Map<String, Object> result = new HashMap<>();
            
            // Find the check-in record
            Optional<Checkin> checkinOpt = checkinRepository.findByFolioNo(folioNo);
            if (checkinOpt.isEmpty()) {
                result.put("success", false);
                result.put("message", "Check-in not found");
                return result;
            }
            
            Checkin checkin = checkinOpt.get();
            
            // Check if already checked out
            if (checkin.getCheckOutDate() == null) {
                result.put("success", false);
                result.put("message", "Guest not checked out yet");
                return result;
            }
            
            // Cancel checkout by setting checkout date to null
            checkin.setCheckOutDate(null);
            checkin.setUpdatedOn(LocalDateTime.now());
            checkinRepository.save(checkin);
            
            result.put("success", true);
            result.put("message", "Checkout cancelled successfully");
            
            logger.info("Checkout cancelled for folio: {}", folioNo);
            return result;
            
        } catch (Exception e) {
            logger.error("Error cancelling checkout: {}", e.getMessage(), e);
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "Error cancelling checkout: " + e.getMessage());
            return result;
        }
    }
} 