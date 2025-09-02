package com.hotel.erp.service;

import com.hotel.erp.dto.ReservationDTO;
import com.hotel.erp.entity.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationService {
    Reservation createReservation(ReservationDTO reservationDTO);

    Reservation updateReservation(Integer id, ReservationDTO reservationDTO);

    List<Reservation> getAllReservations();

    Optional<Reservation> getReservationById(Integer id);

    void deleteReservation(Integer id);

    List<Reservation> findReservationsByGuestName(String guestName);

    List<Reservation> findReservationsByContactNo(String contactNo);

    Optional<Reservation> findReservationByReservationNo(String reservationNo);

    List<Reservation> findReservationsByStatus(String status);
}
