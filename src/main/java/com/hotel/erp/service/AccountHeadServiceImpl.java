package com.hotel.erp.service;

import com.hotel.erp.dto.AccountHeadDTO;
import com.hotel.erp.entity.AccountHead;
import com.hotel.erp.repository.AccountHeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AccountHeadServiceImpl implements AccountHeadService {

    private final AccountHeadRepository accountHeadRepository;

    @Autowired
    public AccountHeadServiceImpl(AccountHeadRepository accountHeadRepository) {
        this.accountHeadRepository = accountHeadRepository;
    }

    @Override
    public AccountHeadDTO createAccountHead(AccountHeadDTO accountHeadDTO) {
        // Check if account head with same name already exists
        if (accountHeadRepository.existsByAccountHeadName(accountHeadDTO.getAccountHeadName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Account head with name " + accountHeadDTO.getAccountHeadName() + " already exists");
        }

        // Convert DTO to entity
        AccountHead accountHead = mapToEntity(accountHeadDTO);

        // Save to database
        AccountHead savedAccountHead = accountHeadRepository.save(accountHead);

        // Convert back to DTO and return
        return mapToDTO(savedAccountHead);
    }

    @Override
    public AccountHeadDTO updateAccountHead(Integer id, AccountHeadDTO accountHeadDTO) {
        // Find existing account head
        AccountHead accountHead = accountHeadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account head with id " + id + " not found"));

        // Check if new name conflicts with existing name (but not this account head)
        if (!accountHead.getAccountHeadName().equals(accountHeadDTO.getAccountHeadName()) &&
                accountHeadRepository.existsByAccountHeadName(accountHeadDTO.getAccountHeadName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Account head with name " + accountHeadDTO.getAccountHeadName() + " already exists");
        }

        // Update entity
        accountHead.setAccountHeadName(accountHeadDTO.getAccountHeadName());
        accountHead.setAccountHeadDescription(accountHeadDTO.getAccountHeadDescription());

        // Save changes
        AccountHead updatedAccountHead = accountHeadRepository.save(accountHead);

        // Convert to DTO and return
        return mapToDTO(updatedAccountHead);
    }

    @Override
    public AccountHeadDTO getAccountHeadById(Integer id) {
        // Find and return account head by ID
        return accountHeadRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account head with id " + id + " not found"));
    }

    @Override
    public AccountHeadDTO getAccountHeadByName(String name) {
        // Find and return account head by name
        return accountHeadRepository.findByAccountHeadName(name)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account head with name " + name + " not found"));
    }

    @Override
    public List<AccountHeadDTO> getAllAccountHeads() {
        // Get all account heads and convert to DTOs
        return accountHeadRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAccountHead(Integer id) {
        // Check if account head exists
        if (!accountHeadRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Account head with id " + id + " not found");
        }

        // Delete account head
        accountHeadRepository.deleteById(id);
    }

    // Helper method to convert entity to DTO
    private AccountHeadDTO mapToDTO(AccountHead accountHead) {
        AccountHeadDTO dto = new AccountHeadDTO();
        dto.setAccountHeadId(accountHead.getAccountHeadId());
        dto.setAccountHeadName(accountHead.getAccountHeadName());
        dto.setAccountHeadDescription(accountHead.getAccountHeadDescription());
        return dto;
    }

    // Helper method to convert DTO to entity
    private AccountHead mapToEntity(AccountHeadDTO dto) {
        AccountHead entity = new AccountHead();
        // Don't set ID when creating a new entity
        if (dto.getAccountHeadId() != null) {
            entity.setAccountHeadId(dto.getAccountHeadId());
        }
        entity.setAccountHeadName(dto.getAccountHeadName());
        entity.setAccountHeadDescription(dto.getAccountHeadDescription());
        return entity;
    }
}
