package com.hotel.erp.controller;

import com.hotel.erp.dto.ArrivalModeDTO;
import com.hotel.erp.service.ArrivalModeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/arrival-modes")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ArrivalModeController {

    private final ArrivalModeService arrivalModeService;

    @Autowired
    public ArrivalModeController(ArrivalModeService arrivalModeService) {
        this.arrivalModeService = arrivalModeService;
    }

    @GetMapping
    public ResponseEntity<List<ArrivalModeDTO>> getAllArrivalModes() {
        try {
            List<ArrivalModeDTO> arrivalModes = arrivalModeService.getAllArrivalModes();
            return ResponseEntity.ok(arrivalModes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArrivalModeDTO> getArrivalModeById(@PathVariable Integer id) {
        try {
            ArrivalModeDTO arrivalMode = arrivalModeService.getArrivalModeById(id);
            return ResponseEntity.ok(arrivalMode);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<ArrivalModeDTO> createArrivalMode(@RequestBody ArrivalModeDTO arrivalModeDTO) {
        try {
            ArrivalModeDTO createdArrivalMode = arrivalModeService.createArrivalMode(arrivalModeDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdArrivalMode);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArrivalModeDTO> updateArrivalMode(@PathVariable Integer id,
            @RequestBody ArrivalModeDTO arrivalModeDTO) {
        try {
            ArrivalModeDTO updatedArrivalMode = arrivalModeService.updateArrivalMode(id, arrivalModeDTO);
            return ResponseEntity.ok(updatedArrivalMode);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArrivalMode(@PathVariable Integer id) {
        try {
            arrivalModeService.deleteArrivalMode(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}