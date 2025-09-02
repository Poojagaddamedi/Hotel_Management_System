package com.hotel.erp.service;

import com.hotel.erp.entity.Checkin;
import com.hotel.erp.entity.Reservation;
import com.hotel.erp.repository.CheckinRepository;
import com.hotel.erp.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class CheckinService {

    @Autowired
    private CheckinRepository checkinRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    public List<Checkin> getAllCheckins() {
        return checkinRepository.findAll();
    }

    public Optional<Checkin> getCheckinById(Long id) {
        return checkinRepository.findById(id);
    }

    public Optional<Checkin> getCheckinByFolioNo(String folioNo) {
        return checkinRepository.findByFolioNo(folioNo);
    }

    public List<Checkin> getAllActiveCheckins() {
        return checkinRepository.findAllActiveCheckins();
    }

    public List<Checkin> getCheckinsByRoomNo(String roomNo) {
        return checkinRepository.findByRoomNo(roomNo);
    }

    public List<Checkin> getCheckinsByCustomerId(Integer customerId) {
        return checkinRepository.findByCustomerId(customerId);
    }

    public List<Checkin> getCheckinsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return checkinRepository.findCheckinsBetweenDates(startDate, endDate);
    }

    public Long countActiveCheckins() {
        return checkinRepository.countActiveCheckins();
    }

    public List<Checkin> getCheckoutsDueToday() {
        return checkinRepository.findByCheckOutDateAndStatus(LocalDate.now(), "CHECKED_IN");
    }

    public Optional<Checkin> getCheckinByReservationNo(String reservationNo) {
        return checkinRepository.findByReservationNo(reservationNo);
    }

    public List<Checkin> getCheckinsByUserId(Integer userId) {
        return checkinRepository.findByUserId(userId);
    }

    public List<Checkin> getOverdueCheckins() {
        return checkinRepository.findOverdueCheckins(LocalDate.now());
    }

    public Checkin saveCheckin(Checkin checkin) {
        // Generate folio number if not provided
        if (checkin.getFolioNo() == null || checkin.getFolioNo().isEmpty()) {
            checkin.setFolioNo(generateFolioNumber());
        }

        // Set creation timestamp
        if (checkin.getCreatedOn() == null) {
            checkin.setCreatedOn(LocalDateTime.now());
        }
        checkin.setUpdatedOn(LocalDateTime.now());

        // Set default status if not provided
        if (checkin.getStatus() == null || checkin.getStatus().isEmpty()) {
            checkin.setStatus("CHECKED_IN");
        }

        return checkinRepository.save(checkin);
    }

    /**
     * Generate a unique folio number in format: FOL/YYYYMM/NNNN
     */
    private String generateFolioNumber() {
        LocalDate today = LocalDate.now();
        String prefix = "FOL/" + today.format(DateTimeFormatter.ofPattern("yyyyMM")) + "/";

        // Find the highest sequence number for current month
        List<Checkin> currentMonthCheckins = checkinRepository.findAll().stream()
                .filter(c -> c.getFolioNo() != null && c.getFolioNo().startsWith(prefix))
                .toList();

        int nextSequence = currentMonthCheckins.size() + 1;

        // Format sequence with leading zeros (4 digits)
        return prefix + String.format("%04d", nextSequence);
    }

    /**
     * Create check-in from existing reservation
     */
    public Checkin checkInFromReservation(String reservationNo, Integer userId) {
        Optional<Reservation> reservationOpt = reservationRepository.findByReservationNo(reservationNo);
        if (reservationOpt.isEmpty()) {
            throw new IllegalArgumentException("Reservation not found: " + reservationNo);
        }

        // Check if already checked in
        Optional<Checkin> existingCheckin = checkinRepository.findByReservationNo(reservationNo);
        if (existingCheckin.isPresent()) {
            throw new IllegalArgumentException("Guest already checked in for reservation: " + reservationNo);
        }

        Reservation reservation = reservationOpt.get();

        // Create checkin from reservation
        Checkin checkin = new Checkin();
        checkin.setReservationNo(reservationNo);
        checkin.setGuestName(reservation.getGuestName());
        checkin.setContactNo(reservation.getContactNo());
        checkin.setEmailId(reservation.getEmailId());
        checkin.setCheckInDate(reservation.getFromDate());
        checkin.setCheckOutDate(reservation.getToDate());
        checkin.setNoOfPersons(reservation.getTotalPax());
        checkin.setRoomNo(reservation.getSelectedRoom());
        checkin.setRate(reservation.getRate());
        checkin.setCompanyName(reservation.getCompanyName());
        checkin.setCustomerId(reservation.getCustomerId());
        checkin.setUserId(userId);
        checkin.setCreatedOn(LocalDateTime.now());
        checkin.setUpdatedOn(LocalDateTime.now());
        checkin.setStatus("CHECKED_IN");
        checkin.setFolioNo(generateFolioNumber());

        return checkinRepository.save(checkin);
    }

    public void deleteCheckin(Long id) {
        checkinRepository.deleteById(id);
    }
}
