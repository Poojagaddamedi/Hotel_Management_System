package com.hotel.erp.dto;

import lombok.Data;

@Data
public class PlanTypeDTO {
    private Integer planId;
    private String planName;
    private Double planRate;
    private String description;
    private Boolean isActive;
}
