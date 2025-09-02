package com.hotel.erp.controller;

import com.hotel.erp.dto.ReservationSourceDTO;
import com.hotel.erp.service.ReservationSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/reservation-sources")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReservationSourceController {

    private final ReservationSourceService reservationSourceService;

    @Autowired
    public ReservationSourceController(ReservationSourceService reservationSourceService) {
        this.reservationSourceService = reservationSourceService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationSourceDTO>> getAllReservationSources() {
        try {
            List<ReservationSourceDTO> reservationSources = reservationSourceService.getAllReservationSources();
            return ResponseEntity.ok(reservationSources);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationSourceDTO> getReservationSourceById(@PathVariable Integer id) {
        try {
            ReservationSourceDTO reservationSource = reservationSourceService.getReservationSourceById(id);
            return ResponseEntity.ok(reservationSource);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<ReservationSourceDTO> createReservationSource(
            @RequestBody ReservationSourceDTO reservationSourceDTO) {
        try {
            ReservationSourceDTO createdReservationSource = reservationSourceService
                    .createReservationSource(reservationSourceDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdReservationSource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationSourceDTO> updateReservationSource(@PathVariable Integer id,
            @RequestBody ReservationSourceDTO reservationSourceDTO) {
        try {
            ReservationSourceDTO updatedReservationSource = reservationSourceService.updateReservationSource(id,
                    reservationSourceDTO);
            return ResponseEntity.ok(updatedReservationSource);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationSource(@PathVariable Integer id) {
        try {
            reservationSourceService.deleteReservationSource(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}