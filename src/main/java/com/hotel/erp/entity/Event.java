package com.hotel.erp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_no", unique = true, nullable = false)
    private String eventNo;

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Column(name = "event_type", nullable = false)
    private String eventType; // WEDDING, CONFERENCE, BIRTHDAY, CORPORATE, BANQUET, etc.

    @Column(name = "client_name", nullable = false)
    private String clientName;

    @Column(name = "client_contact")
    private String clientContact;

    @Column(name = "client_email")
    private String clientEmail;

    @Column(name = "event_date")
    private LocalDate eventDate;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "venue", nullable = false)
    private String venue; // BANQUET_HALL, CONFERENCE_ROOM, POOLSIDE, GARDEN, etc.

    @Column(name = "guest_count")
    private Integer guestCount;

    @Column(name = "expected_guest_count")
    private Integer expectedGuestCount;

    @Column(name = "status", nullable = false)
    private String status; // INQUIRY, CONFIRMED, CANCELLED, COMPLETED

    @Column(name = "setup_type")
    private String setupType; // THEATER, CLASSROOM, U_SHAPE, ROUND_TABLE, etc.

    @Column(name = "catering_required")
    private Boolean cateringRequired = false;

    @Column(name = "decoration_required")
    private Boolean decorationRequired = false;

    @Column(name = "equipment_required", columnDefinition = "TEXT")
    private String equipmentRequired; // JSON string of equipment

    @Column(name = "special_requirements", columnDefinition = "TEXT")
    private String specialRequirements;

    @Column(name = "package_amount")
    private Double packageAmount;

    @Column(name = "additional_charges")
    private Double additionalCharges;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "advance_paid")
    private Double advancePaid;

    @Column(name = "balance_amount")
    private Double balanceAmount;

    @Column(name = "payment_status")
    private String paymentStatus; // PENDING, PARTIAL, PAID, REFUNDED

    @Column(name = "coordinator_name")
    private String coordinatorName;

    @Column(name = "coordinator_contact")
    private String coordinatorContact;

    @Column(name = "setup_time")
    private LocalDateTime setupTime;

    @Column(name = "cleanup_time")
    private LocalDateTime cleanupTime;

    @Column(name = "booking_date")
    private LocalDate bookingDate;

    @Column(name = "cancellation_date")
    private LocalDate cancellationDate;

    @Column(name = "cancellation_reason")
    private String cancellationReason;

    @Column(name = "feedback")
    private String feedback;

    @Column(name = "rating")
    private Integer rating; // 1-5 scale

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
        if (bookingDate == null) {
            bookingDate = LocalDate.now();
        }
        if (auditDate == null) {
            auditDate = LocalDate.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
