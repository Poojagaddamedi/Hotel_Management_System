package com.hotel.erp.controller;

import com.hotel.erp.dto.PaymentModeDTO;
import com.hotel.erp.service.PaymentModeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/payment-modes")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PaymentModeController {

    private final PaymentModeService paymentModeService;

    @Autowired
    public PaymentModeController(PaymentModeService paymentModeService) {
        this.paymentModeService = paymentModeService;
    }

    @GetMapping
    public ResponseEntity<List<PaymentModeDTO>> getAllPaymentModes() {
        try {
            return ResponseEntity.ok(paymentModeService.getAllPaymentModes());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentModeDTO> getPaymentModeById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(paymentModeService.getPaymentModeById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<PaymentModeDTO> createPaymentMode(@RequestBody PaymentModeDTO paymentModeDTO) {
        try {
            return new ResponseEntity<>(paymentModeService.createPaymentMode(paymentModeDTO), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentModeDTO> updatePaymentMode(@PathVariable Integer id,
            @RequestBody PaymentModeDTO paymentModeDTO) {
        try {
            return ResponseEntity.ok(paymentModeService.updatePaymentMode(id, paymentModeDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentMode(@PathVariable Integer id) {
        try {
            paymentModeService.deletePaymentMode(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
