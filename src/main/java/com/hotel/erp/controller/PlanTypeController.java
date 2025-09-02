package com.hotel.erp.controller;

import com.hotel.erp.dto.PlanTypeDTO;
import com.hotel.erp.service.PlanTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/plan-types")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PlanTypeController {

    private final PlanTypeService planTypeService;

    @Autowired
    public PlanTypeController(PlanTypeService planTypeService) {
        this.planTypeService = planTypeService;
    }

    @GetMapping
    public ResponseEntity<List<PlanTypeDTO>> getAllPlanTypes() {
        try {
            return ResponseEntity.ok(planTypeService.getAllPlanTypes());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanTypeDTO> getPlanTypeById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(planTypeService.getPlanTypeById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<PlanTypeDTO> createPlanType(@RequestBody PlanTypeDTO planTypeDTO) {
        try {
            return new ResponseEntity<>(planTypeService.createPlanType(planTypeDTO), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanTypeDTO> updatePlanType(@PathVariable Integer id, @RequestBody PlanTypeDTO planTypeDTO) {
        try {
            return ResponseEntity.ok(planTypeService.updatePlanType(id, planTypeDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlanType(@PathVariable Integer id) {
        try {
            planTypeService.deletePlanType(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
