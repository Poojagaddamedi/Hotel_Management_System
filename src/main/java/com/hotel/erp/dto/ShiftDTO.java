package com.hotel.erp.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ShiftDTO {
    private Long id;
    private Integer shiftNumber;
    private LocalDateTime date;
    private LocalDateTime auditDate;
    private Double openingBalance;
    private Double closingBalance;
    private Double totalIncome;
    private Double totalExpense;
    private String shiftStatus;
    private String shiftName;
}
