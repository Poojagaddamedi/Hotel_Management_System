package com.hotel.erp.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TaxDTO {
    private Integer taxId;
    private String taxName;
    private BigDecimal taxPercentage;
}
