package com.hotel.erp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "company")
@Data
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "company_name", nullable = false, unique = true)
    private String companyName;

    @Column(name = "address1")
    private String address1;

    @Column(name = "address2")
    private String address2;

    @Column(name = "address3")
    private String address3;

    @Column(name = "pincode")
    private String pincode;

    @Column(name = "email_id")
    private String emailId;

    @Column(name = "sms_tele_no")
    private String smsTeleNo;

    @Column(name = "telephone_number2")
    private String telephoneNumber2;

    @Column(name = "gst")
    private String gst;

    @Column(name = "vat")
    private String vat;

    @Column(name = "pan")
    private String pan;

    @Column(name = "schedule_head")
    private String scheduleHead;

    @Column(name = "sub_schedule_head")
    private String subScheduleHead;

    @Column(name = "account_type")
    private Integer accountType;

    @Column(name = "nationality")
    private Integer nationality;
}
