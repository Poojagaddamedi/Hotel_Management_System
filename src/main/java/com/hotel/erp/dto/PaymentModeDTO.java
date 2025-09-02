package com.hotel.erp.dto;

import lombok.Data;

@Data
public class PaymentModeDTO {
    private Integer id;
    private String settlementType;
    private String modeName;
    private String description;
    private Boolean isActive;
}
