package com.hotel.erp.controller;

import com.hotel.erp.dto.RefModeDTO;
import com.hotel.erp.service.RefModeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/ref-modes")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RefModeController {

    private final RefModeService refModeService;

    @Autowired
    public RefModeController(RefModeService refModeService) {
        this.refModeService = refModeService;
    }

    @GetMapping
    public ResponseEntity<List<RefModeDTO>> getAllRefModes() {
        try {
            List<RefModeDTO> refModes = refModeService.getAllRefModes();
            return ResponseEntity.ok(refModes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RefModeDTO> getRefModeById(@PathVariable Integer id) {
        try {
            RefModeDTO refMode = refModeService.getRefModeById(id);
            return ResponseEntity.ok(refMode);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<RefModeDTO> createRefMode(@RequestBody RefModeDTO refModeDTO) {
        try {
            RefModeDTO createdRefMode = refModeService.createRefMode(refModeDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRefMode);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RefModeDTO> updateRefMode(@PathVariable Integer id, @RequestBody RefModeDTO refModeDTO) {
        try {
            RefModeDTO updatedRefMode = refModeService.updateRefMode(id, refModeDTO);
            return ResponseEntity.ok(updatedRefMode);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRefMode(@PathVariable Integer id) {
        try {
            refModeService.deleteRefMode(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}