package com.hotel.erp.service.impl;

import com.hotel.erp.dto.PlanTypeDTO;
import com.hotel.erp.entity.PlanType;
import com.hotel.erp.repository.PlanTypeRepository;
import com.hotel.erp.service.PlanTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanTypeServiceImpl implements PlanTypeService {

    private final PlanTypeRepository planTypeRepository;

    @Autowired
    public PlanTypeServiceImpl(PlanTypeRepository planTypeRepository) {
        this.planTypeRepository = planTypeRepository;
    }

    @Override
    public PlanTypeDTO createPlanType(PlanTypeDTO planTypeDTO) {
        PlanType planType = new PlanType();
        planType.setPlanName(planTypeDTO.getPlanName());
        planType.setPlanRate(planTypeDTO.getPlanRate());
        PlanType savedPlanType = planTypeRepository.save(planType);
        return convertToDTO(savedPlanType);
    }

    @Override
    public PlanTypeDTO updatePlanType(Integer id, PlanTypeDTO planTypeDTO) {
        PlanType planType = planTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan Type not found with id: " + id));

        planType.setPlanName(planTypeDTO.getPlanName());
        planType.setPlanRate(planTypeDTO.getPlanRate());
        PlanType updatedPlanType = planTypeRepository.save(planType);
        return convertToDTO(updatedPlanType);
    }

    @Override
    public PlanTypeDTO getPlanTypeById(Integer id) {
        PlanType planType = planTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan Type not found with id: " + id));
        return convertToDTO(planType);
    }

    @Override
    public PlanTypeDTO getPlanTypeByName(String name) {
        PlanType planType = planTypeRepository.findByPlanName(name)
                .orElseThrow(() -> new RuntimeException("Plan Type not found with name: " + name));
        return convertToDTO(planType);
    }

    @Override
    public List<PlanTypeDTO> getAllPlanTypes() {
        return planTypeRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deletePlanType(Integer id) {
        if (!planTypeRepository.existsById(id)) {
            throw new RuntimeException("Plan Type not found with id: " + id);
        }
        planTypeRepository.deleteById(id);
    }

    private PlanTypeDTO convertToDTO(PlanType planType) {
        PlanTypeDTO dto = new PlanTypeDTO();
        dto.setPlanId(planType.getPlanId());
        dto.setPlanName(planType.getPlanName());
        dto.setPlanRate(planType.getPlanRate());
        dto.setDescription(planType.getPlanName()); // Use plan name as description for now
        dto.setIsActive(true); // Default to true
        return dto;
    }
}