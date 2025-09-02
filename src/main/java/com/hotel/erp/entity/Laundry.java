package com.hotel.erp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "laundry")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Laundry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "laundry_no", unique = true, nullable = false)
    private String laundryNo;

    @Column(name = "guest_name", nullable = false)
    private String guestName;

    @Column(name = "room_no", nullable = false)
    private String roomNo;

    @Column(name = "folio_no")
    private String folioNo;

    @Column(name = "pickup_date")
    private LocalDate pickupDate;

    @Column(name = "pickup_time")
    private LocalDateTime pickupTime;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    @Column(name = "delivery_time")
    private LocalDateTime deliveryTime;

    @Column(name = "status", nullable = false)
    private String status; // PENDING, PICKED_UP, WASHING, DRYING, PRESSING, READY, DELIVERED

    @Column(name = "service_type", nullable = false)
    private String serviceType; // REGULAR, EXPRESS, SAME_DAY, DRY_CLEAN

    @Column(name = "items", columnDefinition = "TEXT")
    private String items; // JSON string of laundry items

    @Column(name = "total_pieces")
    private Integer totalPieces;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "tax_amount")
    private Double taxAmount;

    @Column(name = "discount_amount")
    private Double discountAmount;

    @Column(name = "net_amount")
    private Double netAmount;

    @Column(name = "payment_mode")
    private String paymentMode; // CASH, CARD, ROOM_CHARGE, COMPLIMENTARY

    @Column(name = "payment_status")
    private String paymentStatus; // PENDING, PAID, PARTIAL, CANCELLED

    @Column(name = "special_instructions")
    private String specialInstructions;

    @Column(name = "urgent")
    private Boolean urgent = false;

    @Column(name = "assigned_to")
    private String assignedTo;

    @Column(name = "processed_by")
    private String processedBy;

    @Column(name = "quality_check")
    private Boolean qualityCheck = false;

    @Column(name = "quality_remarks")
    private String qualityRemarks;

    @Column(name = "guest_satisfaction_rating")
    private Integer guestSatisfactionRating; // 1-5 scale

    @Column(name = "guest_feedback")
    private String guestFeedback;

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
        if (pickupTime == null) {
            pickupTime = LocalDateTime.now();
        }
        if (auditDate == null) {
            auditDate = LocalDate.now();
        }
        if (pickupDate == null) {
            pickupDate = LocalDate.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
