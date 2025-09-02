package com.hotel.erp.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "shift")
@Data
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "no")
    private Integer shiftNumber;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "audit_date")
    private LocalDateTime auditDate;

    @Column(name = "opening_balance")
    private Double openingBalance;

    @Column(name = "closing_balance")
    private Double closingBalance;

    @Column(name = "total_income")
    private Double totalIncome;

    @Column(name = "total_expense")
    private Double totalExpense;

    @Column(name = "shiftStatus")
    private String shiftStatus;

    @Column(name = "shiftName")
    private String shiftName;
}
