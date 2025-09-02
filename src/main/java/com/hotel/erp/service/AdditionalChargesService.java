package com.hotel.erp.service;

import com.hotel.erp.dto.AdditionalChargesDTO;
import com.hotel.erp.entity.AdditionalCharges;
import com.hotel.erp.repository.AdditionalChargesRepository;
import com.hotel.erp.repository.CheckinRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.hotel.erp.entity.Checkin;

@Service
public class AdditionalChargesService {

    private static final Logger logger = LoggerFactory.getLogger(AdditionalChargesService.class);

    @Autowired
    private AdditionalChargesRepository additionalChargesRepository;

    @Autowired
    private CheckinRepository checkinRepository;

    /**
     * Create a new additional charge
     */
    public AdditionalCharges createCharge(AdditionalChargesDTO chargeDTO) {
        logger.info("Creating additional charge for folio: {}", chargeDTO.getFolioNo());
        
        try {
            // Validate folio number exists if provided
            if (chargeDTO.getFolioNo() != null && !chargeDTO.getFolioNo().trim().isEmpty()) {
                Optional<Checkin> checkinOpt = checkinRepository.findByFolioNo(chargeDTO.getFolioNo());
                if (checkinOpt.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Folio number does not exist");
                }
            }
            
            // Validate amount is positive
            if (chargeDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be positive");
            }
            
            // Validate charge type is not empty
            if (chargeDTO.getChargeType() == null || chargeDTO.getChargeType().trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Charge type is required");
            }
            
            // Create charge
            AdditionalCharges charge = new AdditionalCharges();
            charge.setFolioNo(chargeDTO.getFolioNo());
            charge.setReservationNo(chargeDTO.getReservationNo());
            charge.setChargeType(chargeDTO.getChargeType());
            charge.setAmount(chargeDTO.getAmount());
            charge.setChargeDate(chargeDTO.getChargeDate() != null ? chargeDTO.getChargeDate() : LocalDate.now());
            charge.setRemarks(chargeDTO.getRemarks());
            charge.setUserId(chargeDTO.getUserId());
            
            AdditionalCharges savedCharge = additionalChargesRepository.save(charge);
            
            logger.info("Additional charge created successfully with ID: {}", savedCharge.getId());
            return savedCharge;
            
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error creating additional charge: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating additional charge");
        }
    }

    /**
     * Get charges by folio number
     */
    public List<AdditionalCharges> getChargesByFolio(String folioNo) {
        try {
            List<AdditionalCharges> charges = additionalChargesRepository.findByFolioNo(folioNo);
            logger.info("Retrieved {} charges for folio: {}", charges.size(), folioNo);
            return charges;
        } catch (Exception e) {
            logger.error("Error retrieving charges: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Get charges by reservation number
     */
    public List<AdditionalCharges> getChargesByReservation(String reservationNo) {
        try {
            List<AdditionalCharges> charges = additionalChargesRepository.findByReservationNo(reservationNo);
            logger.info("Retrieved {} charges for reservation: {}", charges.size(), reservationNo);
            return charges;
        } catch (Exception e) {
            logger.error("Error retrieving charges: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Get charges by charge type
     */
    public List<AdditionalCharges> getChargesByType(String chargeType) {
        try {
            List<AdditionalCharges> charges = additionalChargesRepository.findByChargeType(chargeType);
            logger.info("Retrieved {} charges for type: {}", charges.size(), chargeType);
            return charges;
        } catch (Exception e) {
            logger.error("Error retrieving charges: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Calculate total charges for a folio
     */
    public BigDecimal calculateTotalCharges(String folioNo) {
        try {
            BigDecimal total = additionalChargesRepository.getTotalAmountByFolioNo(folioNo);
            logger.info("Calculated total charges for folio {}: {}", folioNo, total);
            return total;
        } catch (Exception e) {
            logger.error("Error calculating total charges: {}", e.getMessage(), e);
            return BigDecimal.ZERO;
        }
    }

    /**
     * Get charge summary for a folio
     */
    public Map<String, Object> getChargeSummary(String folioNo) {
        try {
            Map<String, Object> summary = new HashMap<>();
            
            List<AdditionalCharges> charges = getChargesByFolio(folioNo);
            BigDecimal total = calculateTotalCharges(folioNo);
            
            summary.put("charges", charges);
            summary.put("totalAmount", total);
            summary.put("chargeCount", charges.size());
            
            logger.info("Retrieved charge summary for folio: {}", folioNo);
            return summary;
            
        } catch (Exception e) {
            logger.error("Error getting charge summary: {}", e.getMessage(), e);
            Map<String, Object> summary = new HashMap<>();
            summary.put("charges", new ArrayList<>());
            summary.put("totalAmount", BigDecimal.ZERO);
            summary.put("chargeCount", 0);
            return summary;
        }
    }

    /**
     * Get all charges
     */
    public List<AdditionalCharges> getAllCharges() {
        try {
            List<AdditionalCharges> charges = additionalChargesRepository.findAll();
            logger.info("Retrieved {} total charges", charges.size());
            return charges;
        } catch (Exception e) {
            logger.error("Error retrieving all charges: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Update a charge
     */
    public AdditionalCharges updateCharge(Long chargeId, AdditionalChargesDTO chargeDTO) {
        logger.info("Updating charge with ID: {}", chargeId);
        
        try {
            Optional<AdditionalCharges> chargeOpt = additionalChargesRepository.findById(chargeId);
            if (chargeOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Charge not found");
            }
            
            AdditionalCharges charge = chargeOpt.get();
            
            // Update fields
            if (chargeDTO.getFolioNo() != null) {
                charge.setFolioNo(chargeDTO.getFolioNo());
            }
            if (chargeDTO.getReservationNo() != null) {
                charge.setReservationNo(chargeDTO.getReservationNo());
            }
            if (chargeDTO.getChargeType() != null) {
                charge.setChargeType(chargeDTO.getChargeType());
            }
            if (chargeDTO.getAmount() != null) {
                charge.setAmount(chargeDTO.getAmount());
            }
            if (chargeDTO.getChargeDate() != null) {
                charge.setChargeDate(chargeDTO.getChargeDate());
            }
            if (chargeDTO.getRemarks() != null) {
                charge.setRemarks(chargeDTO.getRemarks());
            }
            if (chargeDTO.getUserId() != null) {
                charge.setUserId(chargeDTO.getUserId());
            }
            
            AdditionalCharges updatedCharge = additionalChargesRepository.save(charge);
            
            logger.info("Charge updated successfully with ID: {}", updatedCharge.getId());
            return updatedCharge;
            
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error updating charge: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating charge");
        }
    }

    /**
     * Delete a charge
     */
    public void deleteCharge(Long chargeId) {
        logger.info("Deleting charge with ID: {}", chargeId);
        
        try {
            if (!additionalChargesRepository.existsById(chargeId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Charge not found");
            }
            
            additionalChargesRepository.deleteById(chargeId);
            logger.info("Charge deleted successfully with ID: {}", chargeId);
            
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error deleting charge: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting charge");
        }
    }

    /**
     * Search charges
     */
    public List<AdditionalCharges> searchCharges(String query) {
        try {
            List<AdditionalCharges> charges = additionalChargesRepository.searchCharges(query);
            logger.info("Found {} charges matching query: {}", charges.size(), query);
            return charges;
        } catch (Exception e) {
            logger.error("Error searching charges: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Get charges by date range
     */
    public List<AdditionalCharges> getChargesByDateRange(String startDate, String endDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate, formatter);
            
            List<AdditionalCharges> charges = additionalChargesRepository.findByChargeDateBetween(start, end);
            logger.info("Retrieved {} charges between {} and {}", charges.size(), startDate, endDate);
            return charges;
        } catch (Exception e) {
            logger.error("Error retrieving charges by date range: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
} 