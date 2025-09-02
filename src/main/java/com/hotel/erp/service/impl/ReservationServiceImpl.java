package com.hotel.erp.service.impl;

import com.hotel.erp.dto.ReservationDTO;
import com.hotel.erp.entity.AccountYear;
import com.hotel.erp.entity.Reservation;
import com.hotel.erp.entity.Tax;
import com.hotel.erp.exception.ResourceNotFoundException;
import com.hotel.erp.repository.AccountYearRepository;
import com.hotel.erp.repository.ReservationRepository;
import com.hotel.erp.repository.TaxRepository;
import com.hotel.erp.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private AccountYearRepository accountYearRepository;

    @Autowired
    private TaxRepository taxRepository;

    @Override
    @Transactional
    public Reservation createReservation(ReservationDTO reservationDTO) {
        // Validate input
        validateReservationData(reservationDTO);

        Reservation reservation = new Reservation();
        mapDtoToEntity(reservationDTO, reservation);

        // Set automatic fields
        reservation.setReservationNo(generateReservationNumber());
        reservation.setAuditDate(LocalDate.now());
        reservation.setCreatedOn(LocalDateTime.now());
        reservation.setUpdatedOn(LocalDateTime.now());
        reservation.setIsCheckinDone(false);
        reservation.setRoomsCheckIn(0);

        // Calculate total amount if rate is provided
        calculateTotalAmount(reservation);

        return reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public Reservation updateReservation(Integer id, ReservationDTO reservationDTO) {
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));

        // Validate input
        validateReservationData(reservationDTO);

        // Update fields
        mapDtoToEntity(reservationDTO, existingReservation);

        // Update modification timestamp
        existingReservation.setUpdatedOn(LocalDateTime.now());

        // Recalculate total amount if rate is provided
        calculateTotalAmount(existingReservation);

        return reservationRepository.save(existingReservation);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public Optional<Reservation> getReservationById(Integer id) {
        return reservationRepository.findById(id);
    }

    @Override
    @Transactional
    public void deleteReservation(Integer id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation not found with id: " + id);
        }
        reservationRepository.deleteById(id);
    }

    @Override
    public List<Reservation> findReservationsByGuestName(String guestName) {
        return reservationRepository.findByGuestNameContainingIgnoreCase(guestName);
    }

    @Override
    public List<Reservation> findReservationsByContactNo(String contactNo) {
        return reservationRepository.findByContactNoContaining(contactNo);
    }

    @Override
    public Optional<Reservation> findReservationByReservationNo(String reservationNo) {
        return reservationRepository.findByReservationNo(reservationNo);
    }

    @Override
    public List<Reservation> findReservationsByStatus(String status) {
        return reservationRepository.findByStatus(status);
    }

    private String generateReservationNumber() {
        // Get current account year
        AccountYear accountYear = accountYearRepository.findTopByOrderByAccYearDesc()
                .orElseThrow(() -> new RuntimeException("No account year defined in the system"));

        // Get count of existing reservations and add 1
        long count = reservationRepository.count() + 1;

        // Format: "001/XX-YY" where XX-YY is the account year
        return String.format("%03d/%s", count, accountYear.getAccYear());
    }

    private void validateReservationData(ReservationDTO reservationDTO) {
        // Mandatory field validations
        if (reservationDTO.getGuestName() == null || reservationDTO.getGuestName().trim().isEmpty()) {
            throw new IllegalArgumentException("Guest name cannot be empty");
        }

        if (reservationDTO.getContactNo() == null || reservationDTO.getContactNo().trim().isEmpty()) {
            throw new IllegalArgumentException("Contact number cannot be empty");
        }

        if (reservationDTO.getFromDate() == null) {
            throw new IllegalArgumentException("From date cannot be empty");
        }

        if (reservationDTO.getToDate() == null) {
            throw new IllegalArgumentException("To date cannot be empty");
        }

        // Business rule validations
        LocalDate today = LocalDate.now();

        if (reservationDTO.getFromDate().isBefore(today)) {
            throw new IllegalArgumentException("From date cannot be in the past");
        }

        if (reservationDTO.getToDate().isBefore(reservationDTO.getFromDate())) {
            throw new IllegalArgumentException("To date must be after from date");
        }

        if (reservationDTO.getTotalPax() == null || reservationDTO.getTotalPax() < 1) {
            throw new IllegalArgumentException("Total number of persons must be at least 1");
        }

        if (reservationDTO.getNoOfRooms() == null || reservationDTO.getNoOfRooms() < 1) {
            throw new IllegalArgumentException("Number of rooms must be at least 1");
        }
    }

    private void mapDtoToEntity(ReservationDTO dto, Reservation entity) {
        entity.setGuestName(dto.getGuestName());
        entity.setFromDate(dto.getFromDate());
        entity.setToDate(dto.getToDate());
        entity.setCustomerId(dto.getCustomerId());
        entity.setCompanyName(dto.getCompanyName());
        entity.setNoOfRooms(dto.getNoOfRooms());
        entity.setContactNo(dto.getContactNo());
        entity.setEmailId(dto.getEmailId());
        entity.setTotalPax(dto.getTotalPax());
        entity.setRate(dto.getRate());
        entity.setTax(dto.getTax());
        entity.setWithTax(dto.getWithTax() != null ? dto.getWithTax() : "Y");
        entity.setIsTaxInclusive(dto.getIsTaxInclusive() != null ? dto.getIsTaxInclusive() : false);
        entity.setTotalAmt(dto.getTotalAmt());
        entity.setRemarks(dto.getRemarks());
        entity.setPurpose(dto.getPurpose());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : "0");
        entity.setSelectedRoom(dto.getSelectedRoom());
        entity.setNationality(dto.getNationality());
        entity.setExternalRefNo(dto.getExternalRefNo());
        entity.setUserId(dto.getUserId());
    }

    private void calculateTotalAmount(Reservation reservation) {
        if (reservation.getRate() != null) {
            // Calculate number of days
            long days = ChronoUnit.DAYS.between(reservation.getFromDate(), reservation.getToDate());
            if (days < 1)
                days = 1; // Minimum 1 day

            // Calculate base amount
            double baseAmount = reservation.getRate() * days * reservation.getNoOfRooms();

            // Apply tax if not tax inclusive
            if (!"Y".equals(reservation.getWithTax()) || Boolean.FALSE.equals(reservation.getIsTaxInclusive())) {
                // Get tax rate from tax_master
                double taxRate = 0.0;
                List<Tax> taxes = taxRepository.findAll();
                for (Tax tax : taxes) {
                    taxRate += tax.getTaxPercentage().doubleValue() / 100.0;
                }

                double taxAmount = baseAmount * taxRate;
                reservation.setTax(taxAmount);
                reservation.setTotalAmt(baseAmount + taxAmount);
            } else {
                reservation.setTotalAmt(baseAmount);
            }
        }
    }
}
