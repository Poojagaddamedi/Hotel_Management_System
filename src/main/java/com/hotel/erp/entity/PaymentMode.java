package com.hotel.erp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "fo_bill_settlement_types")
@Data
public class PaymentMode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "settlement_type", nullable = false)
    private String settlementType;
}
