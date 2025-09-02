package com.hotel.erp.service.impl;

import com.hotel.erp.dto.ShiftDTO;
import com.hotel.erp.entity.Shift;
import com.hotel.erp.repository.ShiftRepository;
import com.hotel.erp.service.ShiftService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Service
public class ShiftServiceImpl implements ShiftService {

    private final ShiftRepository shiftRepository;

    @Autowired
    public ShiftServiceImpl(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    @Override
    @Transactional
    public ShiftDTO createShift(ShiftDTO shiftDTO) {
        // Set default values if not provided
        if (shiftDTO.getDate() == null) {
            shiftDTO.setDate(LocalDateTime.now());
        }
        if (shiftDTO.getShiftNumber() == null) {
            // Auto-increment shift number
            shiftDTO.setShiftNumber(getNextShiftNumber());
        }
        if (shiftDTO.getShiftStatus() == null) {
            shiftDTO.setShiftStatus("Open");
        }

        Shift shift = mapToEntity(shiftDTO);
        Shift savedShift = shiftRepository.save(shift);
        return mapToDTO(savedShift);
    }

    private Integer getNextShiftNumber() {
        return shiftRepository.findTopByOrderByShiftNumberDesc()
                .map(shift -> shift.getShiftNumber() + 1)
                .orElse(1);
    }

    @Override
    @Transactional
    public ShiftDTO updateShift(Long id, ShiftDTO shiftDTO) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shift not found with ID: " + id));

        if (shiftDTO.getShiftNumber() != null) {
            shift.setShiftNumber(shiftDTO.getShiftNumber());
        }
        if (shiftDTO.getDate() != null) {
            shift.setDate(shiftDTO.getDate());
        }
        if (shiftDTO.getAuditDate() != null) {
            shift.setAuditDate(shiftDTO.getAuditDate());
        }
        if (shiftDTO.getOpeningBalance() != null) {
            shift.setOpeningBalance(shiftDTO.getOpeningBalance());
        }
        if (shiftDTO.getClosingBalance() != null) {
            shift.setClosingBalance(shiftDTO.getClosingBalance());
        }
        if (shiftDTO.getTotalIncome() != null) {
            shift.setTotalIncome(shiftDTO.getTotalIncome());
        }
        if (shiftDTO.getTotalExpense() != null) {
            shift.setTotalExpense(shiftDTO.getTotalExpense());
        }
        if (shiftDTO.getShiftStatus() != null) {
            shift.setShiftStatus(shiftDTO.getShiftStatus());
        }
        if (shiftDTO.getShiftName() != null) {
            shift.setShiftName(shiftDTO.getShiftName());
        }

        Shift updatedShift = shiftRepository.save(shift);
        return mapToDTO(updatedShift);
    }

    @Override
    public ShiftDTO getShiftById(Long id) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shift not found with ID: " + id));
        return mapToDTO(shift);
    }

    @Override
    public List<ShiftDTO> getAllShifts() {
        List<Shift> shifts = shiftRepository.findAll();
        return shifts.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteShift(Long id) {
        if (!shiftRepository.existsById(id)) {
            throw new EntityNotFoundException("Shift not found with ID: " + id);
        }
        shiftRepository.deleteById(id);
    }

    @Override
    public ShiftDTO getShiftByNumber(Integer shiftNumber) {
        Shift shift = shiftRepository.findByShiftNumber(shiftNumber)
                .orElseThrow(() -> new EntityNotFoundException("Shift not found with number: " + shiftNumber));
        return mapToDTO(shift);
    }

    @Override
    public ShiftDTO getLatestShift() {
        Shift shift = shiftRepository.findTopByOrderByShiftNumberDesc()
                .orElseThrow(() -> new EntityNotFoundException("No shifts found"));
        return mapToDTO(shift);
    }

    @Override
    @Transactional
    public ShiftDTO changeAuditDate(Long id, ShiftDTO shiftDTO) {
        if (shiftDTO.getAuditDate() == null) {
            throw new IllegalArgumentException("Audit date cannot be null");
        }

        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shift not found with ID: " + id));

        shift.setAuditDate(shiftDTO.getAuditDate());
        Shift updatedShift = shiftRepository.save(shift);
        return mapToDTO(updatedShift);
    }

    // Helper methods for mapping between Entity and DTO
    private Shift mapToEntity(ShiftDTO shiftDTO) {
        Shift shift = new Shift();
        shift.setId(shiftDTO.getId());
        shift.setShiftNumber(shiftDTO.getShiftNumber());
        shift.setDate(shiftDTO.getDate());
        shift.setAuditDate(shiftDTO.getAuditDate());
        shift.setOpeningBalance(shiftDTO.getOpeningBalance());
        shift.setClosingBalance(shiftDTO.getClosingBalance());
        shift.setTotalIncome(shiftDTO.getTotalIncome());
        shift.setTotalExpense(shiftDTO.getTotalExpense());
        shift.setShiftStatus(shiftDTO.getShiftStatus());
        shift.setShiftName(shiftDTO.getShiftName());
        return shift;
    }

    private ShiftDTO mapToDTO(Shift shift) {
        ShiftDTO shiftDTO = new ShiftDTO();
        shiftDTO.setId(shift.getId());
        shiftDTO.setShiftNumber(shift.getShiftNumber());
        shiftDTO.setDate(shift.getDate());
        shiftDTO.setAuditDate(shift.getAuditDate());
        shiftDTO.setOpeningBalance(shift.getOpeningBalance());
        shiftDTO.setClosingBalance(shift.getClosingBalance());
        shiftDTO.setTotalIncome(shift.getTotalIncome());
        shiftDTO.setTotalExpense(shift.getTotalExpense());
        shiftDTO.setShiftStatus(shift.getShiftStatus());
        shiftDTO.setShiftName(shift.getShiftName());
        return shiftDTO;
    }
}
