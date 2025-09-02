package com.hotel.erp.controller;

import com.hotel.erp.dto.TaxDTO;
import com.hotel.erp.service.TaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/taxes")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TaxController {

    private final TaxService taxService;

    @Autowired
    public TaxController(TaxService taxService) {
        this.taxService = taxService;
    }

    @GetMapping
    public ResponseEntity<List<TaxDTO>> getAllTaxes() {
        return ResponseEntity.ok(taxService.getAllTaxes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaxDTO> getTaxById(@PathVariable Integer id) {
        return ResponseEntity.ok(taxService.getTaxById(id));
    }

    @PostMapping
    public ResponseEntity<TaxDTO> createTax(@RequestBody TaxDTO taxDTO) {
        return new ResponseEntity<>(taxService.createTax(taxDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaxDTO> updateTax(@PathVariable Integer id, @RequestBody TaxDTO taxDTO) {
        return ResponseEntity.ok(taxService.updateTax(id, taxDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTax(@PathVariable Integer id) {
        taxService.deleteTax(id);
        return ResponseEntity.noContent().build();
    }
}
