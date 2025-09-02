package com.hotel.erp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "food_beverage")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodBeverage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no", unique = true, nullable = false)
    private String orderNo;

    @Column(name = "guest_name")
    private String guestName;

    @Column(name = "room_no")
    private String roomNo;

    @Column(name = "folio_no")
    private String folioNo;

    @Column(name = "outlet", nullable = false)
    private String outlet; // RESTAURANT, BAR, ROOM_SERVICE, BANQUET, POOLSIDE

    @Column(name = "order_type", nullable = false)
    private String orderType; // DINE_IN, ROOM_SERVICE, TAKEAWAY, DELIVERY

    @Column(name = "table_no")
    private String tableNo;

    @Column(name = "waiter_name")
    private String waiterName;

    @Column(name = "order_time")
    private LocalDateTime orderTime;

    @Column(name = "delivery_time")
    private LocalDateTime deliveryTime;

    @Column(name = "status", nullable = false)
    private String status; // PENDING, PREPARING, READY, SERVED, COMPLETED, CANCELLED

    @Column(name = "items", columnDefinition = "TEXT")
    private String items; // JSON string of ordered items

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Column(name = "tax_amount")
    private Double taxAmount;

    @Column(name = "service_charge")
    private Double serviceCharge;

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

    @Column(name = "guest_count")
    private Integer guestCount;

    @Column(name = "preparation_time")
    private Integer preparationTime; // in minutes

    @Column(name = "rating")
    private Integer rating; // 1-5 scale

    @Column(name = "feedback")
    private String feedback;

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
        if (orderTime == null) {
            orderTime = LocalDateTime.now();
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
