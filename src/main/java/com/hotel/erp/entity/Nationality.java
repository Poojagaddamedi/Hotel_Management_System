package com.hotel.erp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "nationality")
@Data
public class Nationality {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "country_name", unique = true)
    private String countryName;

    @Column(name = "country_code")
    private String countryCode;
}
