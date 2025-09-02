package com.hotel.erp.controller;

import com.hotel.erp.dto.ReservationDTO;
import com.hotel.erp.entity.Reservation;
import com.hotel.erp.exception.ResourceNotFoundException;
import com.hotel.erp.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    // Create a new reservation
    @PostMapping("/reservations")
    public ResponseEntity<?> createReservation(@RequestBody ReservationDTO reservationDTO) {
        try {
            Reservation createdReservation = reservationService.createReservation(reservationDTO);
            return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all reservations
    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return new ResponseEntity<>(reservationService.getAllReservations(), HttpStatus.OK);
    }

    // Get reservation by ID
    @GetMapping("/reservations/{id}")
    public ResponseEntity<?> getReservationById(@PathVariable Integer id) {
        return reservationService.getReservationById(id)
                .map(reservation -> new ResponseEntity<Object>(reservation, HttpStatus.OK))
                .orElseGet(
                        () -> new ResponseEntity<Object>("Reservation not found with id: " + id, HttpStatus.NOT_FOUND));
    }

    // Update reservation
    @PutMapping("/reservations/{id}")
    public ResponseEntity<?> updateReservation(@PathVariable Integer id, @RequestBody ReservationDTO reservationDTO) {
        try {
            Reservation updatedReservation = reservationService.updateReservation(id, reservationDTO);
            return new ResponseEntity<>(updatedReservation, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete reservation
    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Integer id) {
        try {
            reservationService.deleteReservation(id);
            return new ResponseEntity<>("Reservation deleted successfully", HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Search reservations by guest name, contact number, or reservation number
    @GetMapping("/reservations/search")
    public ResponseEntity<?> searchReservations(@RequestParam String query) {
        List<Reservation> results = new ArrayList<>();

        // Search by guest name
        results.addAll(reservationService.findReservationsByGuestName(query));

        // Search by contact number
        if (results.isEmpty()) {
            results.addAll(reservationService.findReservationsByContactNo(query));
        }

        // Search by reservation number
        if (results.isEmpty()) {
            reservationService.findReservationByReservationNo(query)
                    .ifPresent(results::add);
        }

        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    // Get reservations by status
    @GetMapping("/reservations/status/{status}")
    public ResponseEntity<List<Reservation>> getReservationsByStatus(@PathVariable String status) {
        return new ResponseEntity<>(reservationService.findReservationsByStatus(status), HttpStatus.OK);
    }
}
