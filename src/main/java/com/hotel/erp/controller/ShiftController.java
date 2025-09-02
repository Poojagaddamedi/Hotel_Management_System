package com.hotel.erp.controller;

import com.hotel.erp.dto.ShiftDTO;
import com.hotel.erp.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/shifts")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ShiftController {

    private final ShiftService shiftService;

    @Autowired
    public ShiftController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @GetMapping
    public ResponseEntity<List<ShiftDTO>> getAllShifts() {
        return ResponseEntity.ok(shiftService.getAllShifts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShiftDTO> getShiftById(@PathVariable Long id) {
        return ResponseEntity.ok(shiftService.getShiftById(id));
    }

    @GetMapping("/number/{shiftNumber}")
    public ResponseEntity<ShiftDTO> getShiftByNumber(@PathVariable Integer shiftNumber) {
        return ResponseEntity.ok(shiftService.getShiftByNumber(shiftNumber));
    }

    @GetMapping("/latest")
    public ResponseEntity<ShiftDTO> getLatestShift() {
        return ResponseEntity.ok(shiftService.getLatestShift());
    }

    @PostMapping
    public ResponseEntity<ShiftDTO> createShift(@RequestBody ShiftDTO shiftDTO) {
        return new ResponseEntity<>(shiftService.createShift(shiftDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShiftDTO> updateShift(@PathVariable Long id, @RequestBody ShiftDTO shiftDTO) {
        return ResponseEntity.ok(shiftService.updateShift(id, shiftDTO));
    }

    @PatchMapping("/{id}/audit-date")
    public ResponseEntity<ShiftDTO> changeAuditDate(@PathVariable Long id, @RequestBody ShiftDTO shiftDTO) {
        return ResponseEntity.ok(shiftService.changeAuditDate(id, shiftDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShift(@PathVariable Long id) {
        shiftService.deleteShift(id);
        return ResponseEntity.noContent().build();
    }
}
