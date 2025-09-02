package com.hotel.erp.service;

import com.hotel.erp.dto.BillSettlementDTO;
import com.hotel.erp.dto.FoBillDTO;
import com.hotel.erp.entity.AdditionalCharges;
import com.hotel.erp.entity.Advances;
import com.hotel.erp.entity.BillSettlement;
import com.hotel.erp.entity.Checkin;
import com.hotel.erp.entity.FoBill;
import com.hotel.erp.repository.AdditionalChargesRepository;
import com.hotel.erp.repository.AdvancesRepository;
import com.hotel.erp.repository.BillSettlementRepository;
import com.hotel.erp.repository.CheckinRepository;
import com.hotel.erp.repository.FoBillRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.hotel.erp.exception.ResourceNotFoundException;

@Service
public class BillingService {

    private static final Logger logger = LoggerFactory.getLogger(BillingService.class);

    @Autowired
    private FoBillRepository foBillRepository;

    @Autowired
    private BillSettlementRepository billSettlementRepository;

    @Autowired
    private AdvancesRepository advancesRepository;

    @Autowired
    private AdditionalChargesRepository additionalChargesRepository;

    @Autowired
    private CheckinRepository checkinRepository;

    /**
     * Create a new bill
     * 
     * @param billDTO the bill data
     * @return the created bill
     */
    public FoBill createBill(FoBillDTO billDTO) {
        logger.info("Creating bill for folio: {}", billDTO.getFolioNo());

        try {
            // Validate folio number exists
            Optional<Checkin> checkinOpt = checkinRepository.findByFolioNo(billDTO.getFolioNo());
            if (checkinOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Folio number does not exist");
            }

            // Validate total amount is positive
            if (billDTO.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Total amount must be positive");
            }

            // Create bill
            FoBill bill = new FoBill();
            bill.setFolioNo(billDTO.getFolioNo());
            bill.setTotalAmount(billDTO.getTotalAmount());
            bill.setBillDate(billDTO.getBillDate() != null ? billDTO.getBillDate() : LocalDate.now());
            bill.setUserId(billDTO.getUserId());

            FoBill savedBill = foBillRepository.save(bill);

            logger.info("Bill created successfully with ID: {}", savedBill.getBillId());
            return savedBill;

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error creating bill: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating bill");
        }
    }

    /**
     * Create bill settlement
     * 
     * @param settlementDTO the settlement data
     * @return the created settlement
     */
    public BillSettlement createSettlement(BillSettlementDTO settlementDTO) {
        logger.info("Creating settlement for bill: {}", settlementDTO.getBillId());

        try {
            // Validate bill exists
            Optional<FoBill> billOpt = foBillRepository.findById(settlementDTO.getBillId());
            if (billOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bill not found");
            }

            FoBill bill = billOpt.get();

            // Validate amount is positive
            if (settlementDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be positive");
            }

            // Validate payment mode
            if (settlementDTO.getPaymentMode() == null || settlementDTO.getPaymentMode().trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment mode is required");
            }

            // Create settlement
            BillSettlement settlement = new BillSettlement();
            settlement.setBillId(settlementDTO.getBillId());
            settlement.setAmount(settlementDTO.getAmount());
            settlement.setPaymentDate(
                    settlementDTO.getPaymentDate() != null ? settlementDTO.getPaymentDate() : LocalDate.now());
            settlement.setPaymentMode(settlementDTO.getPaymentMode());
            settlement.setReferenceNo(settlementDTO.getReferenceNo());
            settlement.setRemarks(settlementDTO.getRemarks());
            settlement.setUserId(settlementDTO.getUserId());

            BillSettlement savedSettlement = billSettlementRepository.save(settlement);

            logger.info("Settlement created successfully with ID: {}", savedSettlement.getSettlementId());
            return savedSettlement;

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error creating settlement: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating settlement");
        }
    }

    /**
     * Get bills by folio number
     * 
     * @param folioNo the folio number
     * @return List of bills
     */
    public List<FoBill> getBillsByFolio(String folioNo) {
        try {
            List<FoBill> bills = foBillRepository.findByFolioNo(folioNo);
            logger.info("Retrieved {} bills for folio: {}", bills.size(), folioNo);
            return bills;
        } catch (Exception e) {
            logger.error("Error retrieving bills: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Get settlements by bill ID
     * 
     * @param billId the bill ID
     * @return List of settlements
     */
    public List<BillSettlement> getSettlementsByBill(Long billId) {
        try {
            List<BillSettlement> settlements = billSettlementRepository.findByBillId(billId);
            logger.info("Retrieved {} settlements for bill: {}", settlements.size(), billId);
            return settlements;
        } catch (Exception e) {
            logger.error("Error retrieving settlements: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Calculate bill total including charges
     * 
     * @param folioNo the folio number
     * @return the total bill amount
     */
    public BigDecimal calculateBillTotal(String folioNo) {
        try {
            // Get total advances
            BigDecimal totalAdvances = advancesRepository.getTotalAmountByFolioNo(folioNo);

            // Get total charges
            BigDecimal totalCharges = additionalChargesRepository.getTotalAmountByFolioNo(folioNo);

            // Calculate total
            BigDecimal total = totalAdvances.add(totalCharges);

            logger.info("Bill total for folio {}: {}", folioNo, total);
            return total;

        } catch (Exception e) {
            logger.error("Error calculating bill total: {}", e.getMessage(), e);
            return BigDecimal.ZERO;
        }
    }

    /**
     * Get bill summary for a folio
     * 
     * @param folioNo the folio number
     * @return bill summary
     */
    public Map<String, Object> getBillSummary(String folioNo) {
        logger.info("Getting bill summary for folio: {}", folioNo);

        try {
            Map<String, Object> summary = new HashMap<>();

            // Get bills
            List<FoBill> bills = getBillsByFolio(folioNo);
            summary.put("bills", bills);

            // Get settlements
            List<BillSettlement> settlements = new ArrayList<>();
            for (FoBill bill : bills) {
                settlements.addAll(getSettlementsByBill(bill.getBillId()));
            }
            summary.put("settlements", settlements);

            // Calculate totals
            BigDecimal totalBills = bills.stream()
                    .map(FoBill::getTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            summary.put("totalBills", totalBills);

            BigDecimal totalSettlements = settlements.stream()
                    .map(BillSettlement::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            summary.put("totalSettlements", totalSettlements);

            BigDecimal outstanding = totalBills.subtract(totalSettlements);
            summary.put("outstandingAmount", outstanding);

            logger.info("Bill summary generated for folio: {}", folioNo);
            return summary;

        } catch (Exception e) {
            logger.error("Error getting bill summary: {}", e.getMessage(), e);
            return new HashMap<>();
        }
    }

    /**
     * Get all bills
     * 
     * @return List of all bills
     */
    public List<FoBill> getAllBills() {
        try {
            List<FoBill> bills = foBillRepository.findAll();
            logger.info("Retrieved {} total bills", bills.size());
            return bills;
        } catch (Exception e) {
            logger.error("Error retrieving all bills: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Get all settlements
     * 
     * @return List of all settlements
     */
    public List<BillSettlement> getAllSettlements() {
        try {
            List<BillSettlement> settlements = billSettlementRepository.findAll();
            logger.info("Retrieved {} total settlements", settlements.size());
            return settlements;
        } catch (Exception e) {
            logger.error("Error retrieving all settlements: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Update a bill
     * 
     * @param billId  the bill ID
     * @param billDTO the updated bill data
     * @return the updated bill
     */
    public FoBill updateBill(Long billId, FoBillDTO billDTO) {
        try {
            FoBill existingBill = foBillRepository.findById(billId)
                    .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + billId));

            existingBill.setTotalAmount(billDTO.getTotalAmount());
            existingBill.setBillDate(billDTO.getBillDate());
            existingBill.setUserId(billDTO.getUserId());

            FoBill updatedBill = foBillRepository.save(existingBill);
            logger.info("Bill updated successfully with ID: {}", billId);
            return updatedBill;

        } catch (Exception e) {
            logger.error("Error updating bill: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating bill");
        }
    }

    /**
     * Update a settlement
     * 
     * @param settlementId  the settlement ID
     * @param settlementDTO the updated settlement data
     * @return the updated settlement
     */
    public BillSettlement updateSettlement(Long settlementId, BillSettlementDTO settlementDTO) {
        try {
            BillSettlement existingSettlement = billSettlementRepository.findById(settlementId)
                    .orElseThrow(() -> new ResourceNotFoundException("Settlement not found with id: " + settlementId));

            // Update settlement fields based on DTO
            // Add appropriate setters based on the actual BillSettlement entity

            BillSettlement updatedSettlement = billSettlementRepository.save(existingSettlement);
            logger.info("Settlement updated successfully with ID: {}", settlementId);
            return updatedSettlement;

        } catch (Exception e) {
            logger.error("Error updating settlement: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating settlement");
        }
    }

    /**
     * Delete a bill
     * 
     * @param billId the bill ID
     */
    public void deleteBill(Long billId) {
        try {
            if (!foBillRepository.existsById(billId)) {
                throw new ResourceNotFoundException("Bill not found with id: " + billId);
            }

            foBillRepository.deleteById(billId);
            logger.info("Bill deleted successfully with ID: {}", billId);

        } catch (Exception e) {
            logger.error("Error deleting bill: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting bill");
        }
    }

    /**
     * Delete a settlement
     * 
     * @param settlementId the settlement ID
     */
    public void deleteSettlement(Long settlementId) {
        try {
            if (!billSettlementRepository.existsById(settlementId)) {
                throw new ResourceNotFoundException("Settlement not found with id: " + settlementId);
            }

            billSettlementRepository.deleteById(settlementId);
            logger.info("Settlement deleted successfully with ID: {}", settlementId);

        } catch (Exception e) {
            logger.error("Error deleting settlement: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting settlement");
        }
    }

    /**
     * Save bill directly (for simple entity save)
     */
    public FoBill saveBill(FoBill bill) {
        return foBillRepository.save(bill);
    }
}