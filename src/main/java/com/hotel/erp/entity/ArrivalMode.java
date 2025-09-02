package com.hotel.erp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "arrivalmode")
@Data
public class ArrivalMode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "arr_id")
    private Integer arrId;

    @Column(name = "arrivalmode")
    private String arrivalMode;
}
