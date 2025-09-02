package com.hotel.erp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "checkouts")
public class Checkout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "folio_no", nullable = false, unique = true, length = 50)
    private String folioNo;

    @Column(name = "reservation_no", nullable = false, length = 50)
    private String reservationNo;

    @Column(name = "guest_name", nullable = false, length = 100)
    private String guestName;

    @Column(name = "room_no", nullable = false, length = 10)
    private String roomNo;

    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;

    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate;

    @Column(name = "actual_check_out_time")
    private LocalDateTime actualCheckOutTime;

    @Column(name = "total_nights", nullable = false)
    private Integer totalNights;

    @Column(name = "room_charges", nullable = false, precision = 10, scale = 2)
    private BigDecimal roomCharges = BigDecimal.ZERO;

    @Column(name = "service_charges", nullable = false, precision = 10, scale = 2)
    private BigDecimal serviceCharges = BigDecimal.ZERO;

    @Column(name = "tax_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "discount_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "total_bill_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalBillAmount = BigDecimal.ZERO;

    @Column(name = "advance_paid", nullable = false, precision = 10, scale = 2)
    private BigDecimal advancePaid = BigDecimal.ZERO;

    @Column(name = "balance_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal balanceAmount = BigDecimal.ZERO;

    @Column(name = "payment_mode", length = 50)
    private String paymentMode;

    @Column(name = "payment_reference", length = 100)
    private String paymentReference;

    @Column(name = "payment_status", nullable = false, length = 20)
    private String paymentStatus = "PENDING";

    @Column(name = "checkout_status", nullable = false, length = 20)
    private String checkoutStatus = "IN_PROGRESS";

    @Column(name = "bill_generated", nullable = false)
    private Boolean billGenerated = false;

    @Column(name = "bill_number", length = 50)
    private String billNumber;

    @Column(name = "checkout_remarks", length = 500)
    private String checkoutRemarks;

    @Column(name = "feedback_rating")
    private Integer feedbackRating;

    @Column(name = "feedback_comments", length = 1000)
    private String feedbackComments;

    @Column(name = "checkout_processed_by", nullable = false)
    private Long checkoutProcessedBy;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @Column(name = "audit_date", nullable = false)
    private LocalDate auditDate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // Additional billing details
    @Column(name = "extra_bed_charges", precision = 10, scale = 2)
    private BigDecimal extraBedCharges = BigDecimal.ZERO;

    @Column(name = "early_checkout_penalty", precision = 10, scale = 2)
    private BigDecimal earlyCheckoutPenalty = BigDecimal.ZERO;

    @Column(name = "late_checkout_charges", precision = 10, scale = 2)
    private BigDecimal lateCheckoutCharges = BigDecimal.ZERO;

    @Column(name = "damage_charges", precision = 10, scale = 2)
    private BigDecimal damageCharges = BigDecimal.ZERO;

    @Column(name = "cleaning_charges", precision = 10, scale = 2)
    private BigDecimal cleaningCharges = BigDecimal.ZERO;

    @Column(name = "guest_email", length = 100)
    private String guestEmail;

    @Column(name = "guest_phone", length = 20)
    private String guestPhone;

    @Column(name = "company_name", length = 100)
    private String companyName;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
        this.actualCheckOutTime = LocalDateTime.now();
        if (this.auditDate == null) {
            this.auditDate = LocalDate.now();
        }
        if (this.checkOutDate == null) {
            this.checkOutDate = LocalDate.now();
        }

        // Auto-generate bill number if not provided
        if (this.billNumber == null || this.billNumber.isEmpty()) {
            this.billNumber = "BILL_" + System.currentTimeMillis();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedDate = LocalDateTime.now();
    }

    // Helper method to calculate balance
    public void calculateBalance() {
        this.balanceAmount = this.totalBillAmount.subtract(this.advancePaid);
    }

    // Helper method to calculate total bill
    public void calculateTotalBill() {
        this.totalBillAmount = this.roomCharges
                .add(this.serviceCharges)
                .add(this.taxAmount)
                .add(this.extraBedCharges)
                .add(this.earlyCheckoutPenalty)
                .add(this.lateCheckoutCharges)
                .add(this.damageCharges)
                .add(this.cleaningCharges)
                .subtract(this.discountAmount);

        calculateBalance();
    }
}
