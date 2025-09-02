package com.hotel.erp.service;

import com.hotel.erp.dto.AdvancesDTO;
import com.hotel.erp.entity.Advances;
import com.hotel.erp.entity.Checkin;
import com.hotel.erp.entity.Reservation;
import com.hotel.erp.repository.AdvancesRepository;
import com.hotel.erp.repository.CheckinRepository;
import com.hotel.erp.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AdvancesService {

    private static final Logger logger = LoggerFactory.getLogger(AdvancesService.class);

    @Autowired
    private AdvancesRepository advancesRepository;

    @Autowired
    private CheckinRepository checkinRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private static final List<String> VALID_PAYMENT_MODES = List.of(
            "Cash", "Credit Card", "Debit Card", "UPI", "Bank Transfer", "Cheque");

    /**
     * Normalize payment mode to proper case (case-insensitive matching)
     */
    private String normalizePaymentMode(String paymentMode) {
        if (paymentMode == null)
            return null;

        String normalized = paymentMode.toLowerCase().trim();
        switch (normalized) {
            case "cash":
                return "Cash";
            case "credit card":
            case "creditcard":
            case "card":
                return "Credit Card";
            case "debit card":
            case "debitcard":
                return "Debit Card";
            case "upi":
                return "UPI";
            case "bank transfer":
            case "banktransfer":
            case "transfer":
                return "Bank Transfer";
            case "cheque":
            case "check":
                return "Cheque"; // Add Cheque as valid payment mode
            default:
                return null; // Invalid payment mode
        }
    }

    /**
     * Get all advances
     * 
     * @return List of all advances
     */
    public List<Advances> getAllAdvances() {
        return advancesRepository.findAll();
    }

    /**
     * Get advance by ID
     * 
     * @param id the advance ID
     * @return Optional containing the advance if found
     */
    public Optional<Advances> getAdvanceById(Long id) {
        return advancesRepository.findById(id);
    }

    /**
     * Create a new advance
     * 
     * @param advancesDTO the advance data
     * @return the created advance
     * @throws IllegalArgumentException if validation fails
     */
    public Advances createAdvance(AdvancesDTO advancesDTO) {
        logger.info("Creating advance with data: {}", advancesDTO);

        try {
            validateAdvanceData(advancesDTO);
            logger.info("Validation passed for advance creation");

            Advances advance = new Advances();
            advance.setReservationNo(advancesDTO.getReservationNo());
            advance.setAuditDate(advancesDTO.getAuditDate());
            advance.setPaymentDate(advancesDTO.getPaymentDate());
            advance.setReferenceNo(advancesDTO.getReferenceNo());
            advance.setPaymentMode(advancesDTO.getPaymentMode());
            advance.setAmount(advancesDTO.getAmount());
            advance.setCreditCardCompany(advancesDTO.getCreditCardCompany());
            advance.setCreditCardNo(advancesDTO.getCreditCardNo());
            advance.setRemarks(advancesDTO.getRemarks());
            advance.setFolioNo(advancesDTO.getFolioNo());
            advance.setShiftNo(advancesDTO.getShiftNo());
            advance.setShiftDate(advancesDTO.getShiftDate());
            advance.setRoomNo(advancesDTO.getRoomNo());
            advance.setUserId(advancesDTO.getUserId());
            advance.setBillNo(advancesDTO.getBillNo());
            advance.setGuestName(advancesDTO.getGuestName());

            Advances savedAdvance = advancesRepository.save(advance);
            logger.info("Advance created successfully with ID: {}", savedAdvance.getAdvancesId());
            return savedAdvance;

        } catch (Exception e) {
            logger.error("Error creating advance: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Update an existing advance
     * 
     * @param id          the advance ID
     * @param advancesDTO the updated advance data
     * @return the updated advance
     * @throws IllegalArgumentException if validation fails or advance not found
     */
    public Advances updateAdvance(Long id, AdvancesDTO advancesDTO) {
        logger.info("Updating advance with ID: {} and data: {}", id, advancesDTO);

        try {
            Optional<Advances> existingAdvance = advancesRepository.findById(id);
            if (existingAdvance.isEmpty()) {
                throw new IllegalArgumentException("Advance not found with ID: " + id);
            }

            validateAdvanceData(advancesDTO);
            logger.info("Validation passed for advance update");

            Advances advance = existingAdvance.get();
            advance.setReservationNo(advancesDTO.getReservationNo());
            advance.setAuditDate(advancesDTO.getAuditDate());
            advance.setPaymentDate(advancesDTO.getPaymentDate());
            advance.setReferenceNo(advancesDTO.getReferenceNo());
            advance.setPaymentMode(advancesDTO.getPaymentMode());
            advance.setAmount(advancesDTO.getAmount());
            advance.setCreditCardCompany(advancesDTO.getCreditCardCompany());
            advance.setCreditCardNo(advancesDTO.getCreditCardNo());
            advance.setRemarks(advancesDTO.getRemarks());
            advance.setFolioNo(advancesDTO.getFolioNo());
            advance.setShiftNo(advancesDTO.getShiftNo());
            advance.setShiftDate(advancesDTO.getShiftDate());
            advance.setRoomNo(advancesDTO.getRoomNo());
            advance.setUserId(advancesDTO.getUserId());
            advance.setBillNo(advancesDTO.getBillNo());
            advance.setGuestName(advancesDTO.getGuestName());

            Advances savedAdvance = advancesRepository.save(advance);
            logger.info("Advance updated successfully with ID: {}", savedAdvance.getAdvancesId());
            return savedAdvance;

        } catch (Exception e) {
            logger.error("Error updating advance: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Delete an advance
     * 
     * @param id the advance ID
     * @throws IllegalArgumentException if advance not found
     */
    public void deleteAdvance(Long id) {
        if (!advancesRepository.existsById(id)) {
            throw new IllegalArgumentException("Advance not found with ID: " + id);
        }
        advancesRepository.deleteById(id);
    }

    /**
     * Search advances by query
     * 
     * @param query the search query
     * @return List of advances matching the query
     */
    public List<Advances> searchAdvances(String query) {
        return advancesRepository.searchAdvances(query);
    }

    /**
     * Get advances by folio number
     * 
     * @param folioNo the folio number
     * @return List of advances for the given folio number
     */
    public List<Advances> getAdvancesByFolioNo(String folioNo) {
        return advancesRepository.findByFolioNo(folioNo);
    }

    /**
     * Get advances by reservation number
     * 
     * @param reservationNo the reservation number
     * @return List of advances for the given reservation number
     */
    public List<Advances> getAdvancesByReservationNo(String reservationNo) {
        return advancesRepository.findByReservationNo(reservationNo);
    }

    /**
     * Get advances by payment mode
     * 
     * @param paymentMode the payment mode
     * @return List of advances for the given payment mode
     */
    public List<Advances> getAdvancesByPaymentMode(String paymentMode) {
        return advancesRepository.findByPaymentMode(paymentMode);
    }

    /**
     * Get advances by payment date
     * 
     * @param paymentDate the payment date
     * @return List of advances for the given payment date
     */
    public List<Advances> getAdvancesByPaymentDate(LocalDate paymentDate) {
        return advancesRepository.findByPaymentDate(paymentDate);
    }

    /**
     * Get advances between dates
     * 
     * @param startDate the start date
     * @param endDate   the end date
     * @return List of advances between the given dates
     */
    public List<Advances> getAdvancesBetweenDates(LocalDate startDate, LocalDate endDate) {
        return advancesRepository.findByPaymentDateBetween(startDate, endDate);
    }

    /**
     * Get total amount by folio number
     * 
     * @param folioNo the folio number
     * @return total amount for the folio number
     */
    public BigDecimal getTotalAmountByFolioNo(String folioNo) {
        return advancesRepository.getTotalAmountByFolioNo(folioNo);
    }

    /**
     * Get total amount by reservation number
     * 
     * @param reservationNo the reservation number
     * @return total amount for the reservation
     */
    public BigDecimal getTotalAmountByReservationNo(String reservationNo) {
        return advancesRepository.getTotalAmountByReservationNo(reservationNo);
    }

    /**
     * Get total amount by payment mode
     * 
     * @param paymentMode the payment mode
     * @return total amount for the payment mode
     */
    public BigDecimal getTotalAmountByPaymentMode(String paymentMode) {
        return advancesRepository.getTotalAmountByPaymentMode(paymentMode);
    }

    /**
     * Get total amount by date range
     * 
     * @param startDate the start date
     * @param endDate   the end date
     * @return total amount for the date range
     */
    public BigDecimal getTotalAmountByDateRange(LocalDate startDate, LocalDate endDate) {
        return advancesRepository.getTotalAmountByDateRange(startDate, endDate);
    }

    /**
     * Validate advance data
     * 
     * @param advancesDTO the advance data to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateAdvanceData(AdvancesDTO advancesDTO) {
        logger.info("Validating advance data: {}", advancesDTO);

        // Validate mandatory fields
        if (advancesDTO.getAmount() == null || advancesDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("Validation failed: Amount must be positive");
            throw new IllegalArgumentException("Amount must be positive");
        }

        if (advancesDTO.getPaymentDate() == null) {
            logger.error("Validation failed: Payment date is required");
            throw new IllegalArgumentException("Payment date is required");
        }

        if (advancesDTO.getPaymentDate().isAfter(LocalDate.now())) {
            logger.error("Validation failed: Payment date cannot be in the future");
            throw new IllegalArgumentException("Payment date cannot be in the future");
        }

        if (advancesDTO.getPaymentMode() == null || advancesDTO.getPaymentMode().trim().isEmpty()) {
            logger.error("Validation failed: Payment mode is required");
            throw new IllegalArgumentException("Payment mode is required");
        }

        // Normalize payment mode to proper case
        String normalizedPaymentMode = normalizePaymentMode(advancesDTO.getPaymentMode().trim());
        if (normalizedPaymentMode == null) {
            logger.error("Validation failed: Invalid payment mode: {}", advancesDTO.getPaymentMode());
            throw new IllegalArgumentException("Invalid payment mode. Valid modes are: " + VALID_PAYMENT_MODES);
        }
        advancesDTO.setPaymentMode(normalizedPaymentMode);

        // BUSINESS REQUIREMENT: Support two scenarios for advances:
        // 1. Pre-checkin: Only reservationNo required (advance before folio creation)
        // 2. Post-checkin: Either folioNo or reservationNo required (but folio
        // preferred)
        boolean hasFolio = advancesDTO.getFolioNo() != null && !advancesDTO.getFolioNo().trim().isEmpty();
        boolean hasReservation = advancesDTO.getReservationNo() != null
                && !advancesDTO.getReservationNo().trim().isEmpty();

        // At least one identifier is required
        if (!hasFolio && !hasReservation) {
            logger.error("Validation failed: Either folio number or reservation number is required");
            throw new IllegalArgumentException(
                    "Either folio number or reservation number is required for advance payments");
        }

        // Validate folio number if provided
        if (hasFolio) {
            Optional<Checkin> checkin = checkinRepository.findFirstByFolioNo(advancesDTO.getFolioNo());
            if (checkin.isEmpty()) {
                logger.error("Validation failed: Folio number does not exist: {}", advancesDTO.getFolioNo());
                throw new IllegalArgumentException("Folio number does not exist in checkin table");
            }

            // Validate payment date aligns with checkin arrival date
            Checkin checkinData = checkin.get();
            if (checkinData.getCheckInDate() != null
                    && advancesDTO.getPaymentDate().isBefore(checkinData.getCheckInDate())) {
                logger.error("Validation failed: Payment date cannot be before check-in date");
                throw new IllegalArgumentException("Payment date cannot be before check-in date");
            }

            // Auto-populate guest name and room number from checkin if not provided
            if ((advancesDTO.getGuestName() == null || advancesDTO.getGuestName().trim().isEmpty()) &&
                    checkinData.getGuestName() != null) {
                advancesDTO.setGuestName(checkinData.getGuestName());
                logger.info("Auto-populated guest name from checkin: {}", advancesDTO.getGuestName());
            }

            if ((advancesDTO.getRoomNo() == null || advancesDTO.getRoomNo().trim().isEmpty()) &&
                    checkinData.getRoomNo() != null) {
                advancesDTO.setRoomNo(checkinData.getRoomNo());
                logger.info("Auto-populated room number from checkin: {}", advancesDTO.getRoomNo());
            }

            // Cross-validation: If both folio and reservation are provided, they must be
            // linked
            if (hasReservation && !checkinData.getReservationNo().equals(advancesDTO.getReservationNo())) {
                logger.error("Validation failed: Folio number {} is not linked to reservation number {}",
                        advancesDTO.getFolioNo(), advancesDTO.getReservationNo());
                throw new IllegalArgumentException("Folio number must be linked to the provided reservation number");
            }
        }

        // Validate reservation number if provided
        if (hasReservation) {
            Optional<Reservation> reservation = reservationRepository
                    .findByReservationNo(advancesDTO.getReservationNo());
            if (reservation.isEmpty()) {
                logger.error("Validation failed: Reservation number does not exist: {}",
                        advancesDTO.getReservationNo());
                throw new IllegalArgumentException("Reservation number does not exist in reservation table");
            }

            // Validate payment date aligns with reservation from date
            Reservation reservationData = reservation.get();
            if (reservationData.getFromDate() != null
                    && advancesDTO.getPaymentDate().isBefore(reservationData.getFromDate())) {
                logger.error("Validation failed: Payment date cannot be before reservation from date");
                throw new IllegalArgumentException("Payment date cannot be before reservation from date");
            }

            // Auto-populate guest name from reservation if not provided and no folio
            if (!hasFolio && (advancesDTO.getGuestName() == null || advancesDTO.getGuestName().trim().isEmpty()) &&
                    reservationData.getGuestName() != null) {
                advancesDTO.setGuestName(reservationData.getGuestName());
                logger.info("Auto-populated guest name from reservation: {}", advancesDTO.getGuestName());
            }
        }

        // Validate credit card fields if payment mode is Credit Card
        if ("Credit Card".equals(advancesDTO.getPaymentMode())) {
            if (advancesDTO.getCreditCardCompany() == null || advancesDTO.getCreditCardCompany().trim().isEmpty()) {
                logger.error("Validation failed: Credit card company is required for credit card payments");
                throw new IllegalArgumentException("Credit card company is required for credit card payments");
            }
            if (advancesDTO.getCreditCardNo() == null || advancesDTO.getCreditCardNo().trim().isEmpty()) {
                logger.error("Validation failed: Credit card number is required for credit card payments");
                throw new IllegalArgumentException("Credit card number is required for credit card payments");
            }
        }

        logger.info("Validation passed successfully");
    }

    /**
     * Save advance directly (for simple entity save)
     */
    public Advances saveAdvance(Advances advance) {
        return advancesRepository.save(advance);
    }

    /**
     * Get advances by guest name
     */
    public List<Advances> getAdvancesByGuestName(String guestName) {
        return advancesRepository.findByGuestName(guestName);
    }
}