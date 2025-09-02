package com.hotel.erp.controller;

import com.hotel.erp.dto.AccountHeadDTO;
import com.hotel.erp.service.AccountHeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/account-heads")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AccountHeadController {

    private final AccountHeadService accountHeadService;

    @Autowired
    public AccountHeadController(AccountHeadService accountHeadService) {
        this.accountHeadService = accountHeadService;
    }

    @GetMapping
    public ResponseEntity<List<AccountHeadDTO>> getAllAccountHeads() {
        return ResponseEntity.ok(accountHeadService.getAllAccountHeads());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountHeadDTO> getAccountHeadById(@PathVariable Integer id) {
        return ResponseEntity.ok(accountHeadService.getAccountHeadById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<AccountHeadDTO> getAccountHeadByName(@PathVariable String name) {
        return ResponseEntity.ok(accountHeadService.getAccountHeadByName(name));
    }

    @PostMapping
    public ResponseEntity<AccountHeadDTO> createAccountHead(@RequestBody AccountHeadDTO accountHeadDTO) {
        return new ResponseEntity<>(accountHeadService.createAccountHead(accountHeadDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountHeadDTO> updateAccountHead(@PathVariable Integer id,
            @RequestBody AccountHeadDTO accountHeadDTO) {
        return ResponseEntity.ok(accountHeadService.updateAccountHead(id, accountHeadDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountHead(@PathVariable Integer id) {
        accountHeadService.deleteAccountHead(id);
        return ResponseEntity.noContent().build();
    }
}
