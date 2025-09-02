package com.hotel.erp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "plantype")
@Data
public class PlanType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Integer planId;

    @Column(name = "plan_name", nullable = false)
    private String planName;

    @Column(name = "planrate", nullable = false)
    private Double planRate;
}
