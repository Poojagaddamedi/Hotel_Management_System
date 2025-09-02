package com.hotel.erp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vendors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vendor_code", unique = true, nullable = false)
    private String vendorCode;

    @Column(name = "vendor_name", nullable = false)
    private String vendorName;

    @Column(name = "vendor_type", nullable = false)
    private String vendorType; // SUPPLIER, SERVICE_PROVIDER, CONTRACTOR, CONSULTANT

    @Column(name = "category", nullable = false)
    private String category; // FOOD, BEVERAGE, MAINTENANCE, LAUNDRY, HOUSEKEEPING, IT, etc.

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "gst_number")
    private String gstNumber;

    @Column(name = "pan_number")
    private String panNumber;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_account_number")
    private String bankAccountNumber;

    @Column(name = "ifsc_code")
    private String ifscCode;

    @Column(name = "payment_terms")
    private String paymentTerms; // NET_30, NET_60, IMMEDIATE, ADVANCE

    @Column(name = "credit_limit")
    private Double creditLimit;

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE, INACTIVE, BLACKLISTED, SUSPENDED

    @Column(name = "rating")
    private Integer rating; // 1-5 scale

    @Column(name = "contract_start_date")
    private LocalDate contractStartDate;

    @Column(name = "contract_end_date")
    private LocalDate contractEndDate;

    @Column(name = "emergency_contact")
    private String emergencyContact;

    @Column(name = "service_areas", columnDefinition = "TEXT")
    private String serviceAreas; // JSON string of service areas

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "audit_date")
    private LocalDate auditDate;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (auditDate == null) {
            auditDate = LocalDate.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
