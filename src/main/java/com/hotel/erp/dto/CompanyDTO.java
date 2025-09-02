package com.hotel.erp.dto;

import lombok.Data;

@Data
public class CompanyDTO {
    private Integer id;
    private String companyName;
    private String address1;
    private String address2;
    private String address3;
    private String pincode;
    private String emailId;
    private String smsTeleNo;
    private String telephoneNumber2;
    private String gst;
    private String vat;
    private String pan;
    private String scheduleHead;
    private String subScheduleHead;
    private Integer accountType;
    private Integer nationality;
    private String nationalityName; // For convenience
}
