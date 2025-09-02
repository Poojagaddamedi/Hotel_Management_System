package com.hotel.erp.service.impl;

import com.hotel.erp.dto.PaymentModeDTO;
import com.hotel.erp.entity.PaymentMode;
import com.hotel.erp.repository.PaymentModeRepository;
import com.hotel.erp.service.PaymentModeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentModeServiceImpl implements PaymentModeService {

    private final PaymentModeRepository paymentModeRepository;

    @Autowired
    public PaymentModeServiceImpl(PaymentModeRepository paymentModeRepository) {
        this.paymentModeRepository = paymentModeRepository;
    }

    @Override
    public PaymentModeDTO createPaymentMode(PaymentModeDTO paymentModeDTO) {
        PaymentMode paymentMode = new PaymentMode();
        paymentMode.setSettlementType(paymentModeDTO.getSettlementType() != null ? paymentModeDTO.getSettlementType()
                : paymentModeDTO.getModeName());
        PaymentMode savedPaymentMode = paymentModeRepository.save(paymentMode);
        return convertToDTO(savedPaymentMode);
    }

    @Override
    public PaymentModeDTO updatePaymentMode(Integer id, PaymentModeDTO paymentModeDTO) {
        PaymentMode paymentMode = paymentModeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment Mode not found with id: " + id));

        paymentMode.setSettlementType(paymentModeDTO.getSettlementType() != null ? paymentModeDTO.getSettlementType()
                : paymentModeDTO.getModeName());
        PaymentMode updatedPaymentMode = paymentModeRepository.save(paymentMode);
        return convertToDTO(updatedPaymentMode);
    }

    @Override
    public PaymentModeDTO getPaymentModeById(Integer id) {
        PaymentMode paymentMode = paymentModeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment Mode not found with id: " + id));
        return convertToDTO(paymentMode);
    }

    @Override
    public PaymentModeDTO getPaymentModeByType(String type) {
        PaymentMode paymentMode = paymentModeRepository.findBySettlementType(type)
                .orElseThrow(() -> new RuntimeException("Payment Mode not found with type: " + type));
        return convertToDTO(paymentMode);
    }

    @Override
    public List<PaymentModeDTO> getAllPaymentModes() {
        return paymentModeRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deletePaymentMode(Integer id) {
        if (!paymentModeRepository.existsById(id)) {
            throw new RuntimeException("Payment Mode not found with id: " + id);
        }
        paymentModeRepository.deleteById(id);
    }

    private PaymentModeDTO convertToDTO(PaymentMode paymentMode) {
        PaymentModeDTO dto = new PaymentModeDTO();
        dto.setId(paymentMode.getId());
        dto.setSettlementType(paymentMode.getSettlementType());
        dto.setModeName(paymentMode.getSettlementType()); // Set both for compatibility
        dto.setDescription(paymentMode.getSettlementType());
        dto.setIsActive(true); // Default to true since no isActive field in entity
        return dto;
    }
}