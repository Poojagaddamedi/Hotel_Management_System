package com.hotel.erp.repository;

import com.hotel.erp.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByGuestNameContainingIgnoreCase(String guestName);

    List<Reservation> findByContactNoContaining(String contactNo);

    Optional<Reservation> findByReservationNo(String reservationNo);

    List<Reservation> findByStatus(String status);
}
