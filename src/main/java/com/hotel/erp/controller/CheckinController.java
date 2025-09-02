package com.hotel.erp.controller;

import com.hotel.erp.entity.Checkin;
import com.hotel.erp.service.CheckinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/checkins")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CheckinController {

    @Autowired
    private CheckinService checkinService;

    @GetMapping
    public ResponseEntity<List<Checkin>> getAllCheckins() {
        return ResponseEntity.ok(checkinService.getAllCheckins());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Checkin> getCheckinById(@PathVariable Long id) {
        return checkinService.getCheckinById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/folio/{folioNo}")
    public ResponseEntity<Checkin> getCheckinByFolioNo(@PathVariable String folioNo) {
        return checkinService.getCheckinByFolioNo(folioNo)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Checkin>> getAllActiveCheckins() {
        return ResponseEntity.ok(checkinService.getAllActiveCheckins());
    }

    @GetMapping("/room/{roomNo}")
    public ResponseEntity<List<Checkin>> getCheckinsByRoomNo(@PathVariable String roomNo) {
        return ResponseEntity.ok(checkinService.getCheckinsByRoomNo(roomNo));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Checkin>> getCheckinsByCustomerId(@PathVariable Integer customerId) {
        return ResponseEntity.ok(checkinService.getCheckinsByCustomerId(customerId));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Checkin>> getCheckinsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(checkinService.getCheckinsBetweenDates(startDate, endDate));
    }

    @GetMapping("/count/active")
    public ResponseEntity<Long> countActiveCheckins() {
        return ResponseEntity.ok(checkinService.countActiveCheckins());
    }

    @GetMapping("/checkout-due")
    public ResponseEntity<List<Checkin>> getCheckoutsDueToday() {
        return ResponseEntity.ok(checkinService.getCheckoutsDueToday());
    }

    @GetMapping("/reservation/{reservationNo}")
    public ResponseEntity<Checkin> getCheckinByReservationNo(@PathVariable String reservationNo) {
        return checkinService.getCheckinByReservationNo(reservationNo)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Checkin>> getCheckinsByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(checkinService.getCheckinsByUserId(userId));
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<Checkin>> getOverdueCheckins() {
        return ResponseEntity.ok(checkinService.getOverdueCheckins());
    }

    @PostMapping
    public ResponseEntity<Checkin> createCheckin(@RequestBody Checkin checkin) {
        return new ResponseEntity<>(checkinService.saveCheckin(checkin), HttpStatus.CREATED);
    }

    @PostMapping("/from-reservation/{reservationNo}")
    public ResponseEntity<?> createCheckinFromReservation(
            @PathVariable String reservationNo,
            @RequestBody CheckinFromReservationRequest request) {
        try {
            Checkin checkin = checkinService.checkInFromReservation(reservationNo, request.getUserId());
            return new ResponseEntity<>(checkin, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    // DTO for checkin from reservation request
    public static class CheckinFromReservationRequest {
        private Integer userId;

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Checkin> updateCheckin(@PathVariable Long id, @RequestBody Checkin checkin) {
        return checkinService.getCheckinById(id)
                .map(existingCheckin -> {
                    checkin.setId(id);
                    return ResponseEntity.ok(checkinService.saveCheckin(checkin));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCheckin(@PathVariable Long id) {
        return checkinService.getCheckinById(id)
                .map(checkin -> {
                    checkinService.deleteCheckin(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
