package com.hotel.erp.service;

import com.hotel.erp.dto.PlanTypeDTO;
import java.util.List;

public interface PlanTypeService {
    PlanTypeDTO createPlanType(PlanTypeDTO planTypeDTO);

    PlanTypeDTO updatePlanType(Integer id, PlanTypeDTO planTypeDTO);

    PlanTypeDTO getPlanTypeById(Integer id);

    PlanTypeDTO getPlanTypeByName(String name);

    List<PlanTypeDTO> getAllPlanTypes();

    void deletePlanType(Integer id);
}
