package com.hotel.erp.service.impl;

import com.hotel.erp.dto.TaxDTO;
import com.hotel.erp.entity.Tax;
import com.hotel.erp.repository.TaxRepository;
import com.hotel.erp.service.TaxService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.persistence.EntityNotFoundException;

@Service
public class TaxServiceImpl implements TaxService {

    private final TaxRepository taxRepository;

    @Autowired
    public TaxServiceImpl(TaxRepository taxRepository) {
        this.taxRepository = taxRepository;
    }

    @Override
    @Transactional
    public TaxDTO createTax(TaxDTO taxDTO) {
        Tax tax = mapToEntity(taxDTO);
        Tax savedTax = taxRepository.save(tax);
        return mapToDTO(savedTax);
    }

    @Override
    @Transactional
    public TaxDTO updateTax(Integer id, TaxDTO taxDTO) {
        Tax tax = taxRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tax not found with ID: " + id));

        tax.setTaxName(taxDTO.getTaxName());
        tax.setTaxPercentage(taxDTO.getTaxPercentage());

        Tax updatedTax = taxRepository.save(tax);
        return mapToDTO(updatedTax);
    }

    @Override
    public TaxDTO getTaxById(Integer id) {
        Tax tax = taxRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tax not found with ID: " + id));
        return mapToDTO(tax);
    }

    @Override
    public List<TaxDTO> getAllTaxes() {
        List<Tax> taxes = taxRepository.findAll();
        return taxes.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteTax(Integer id) {
        if (!taxRepository.existsById(id)) {
            throw new EntityNotFoundException("Tax not found with ID: " + id);
        }
        taxRepository.deleteById(id);
    }

    // Helper methods for mapping between Entity and DTO
    private Tax mapToEntity(TaxDTO taxDTO) {
        Tax tax = new Tax();
        tax.setTaxId(taxDTO.getTaxId());
        tax.setTaxName(taxDTO.getTaxName());
        tax.setTaxPercentage(taxDTO.getTaxPercentage());
        return tax;
    }

    private TaxDTO mapToDTO(Tax tax) {
        TaxDTO taxDTO = new TaxDTO();
        taxDTO.setTaxId(tax.getTaxId());
        taxDTO.setTaxName(tax.getTaxName());
        taxDTO.setTaxPercentage(tax.getTaxPercentage());
        return taxDTO;
    }
}
