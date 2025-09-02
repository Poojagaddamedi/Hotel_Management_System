package com.hotel.erp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "resv_mode")
@Data
public class ReservationSource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "res_id")
    private Integer resId;

    @Column(name = "res_source")
    private String resSource;
}
