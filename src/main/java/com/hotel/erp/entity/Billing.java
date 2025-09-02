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
@Table(name = "billings")
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bill_no", nullable = false, unique = true, length = 50)
    private String billNo;

    @Column(name = "folio_no", nullable = false, length = 50)
    private String folioNo;

    @Column(name = "reservation_no", nullable = false, length = 50)
    private String reservationNo;

    @Column(name = "guest_name", nullable = false, length = 100)
    private String guestName;

    @Column(name = "room_no", nullable = false, length = 10)
    private String roomNo;

    @Column(name = "guest_email", length = 100)
    private String guestEmail;

    @Column(name = "guest_phone", length = 20)
    private String guestPhone;

    @Column(name = "guest_address", length = 300)
    private String guestAddress;

    @Column(name = "company_name", length = 100)
    private String companyName;

    @Column(name = "company_gst", length = 15)
    private String companyGst;

    @Column(name = "bill_date", nullable = false)
    private LocalDate billDate;

    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;

    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate;

    @Column(name = "total_nights", nullable = false)
    private Integer totalNights;

    // Room charges breakdown
    @Column(name = "room_rent", nullable = false, precision = 10, scale = 2)
    private BigDecimal roomRent = BigDecimal.ZERO;

    @Column(name = "extra_bed_charges", precision = 10, scale = 2)
    private BigDecimal extraBedCharges = BigDecimal.ZERO;

    @Column(name = "total_room_charges", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalRoomCharges = BigDecimal.ZERO;

    // Service charges breakdown
    @Column(name = "food_beverage_charges", precision = 10, scale = 2)
    private BigDecimal foodBeverageCharges = BigDecimal.ZERO;

    @Column(name = "laundry_charges", precision = 10, scale = 2)
    private BigDecimal laundryCharges = BigDecimal.ZERO;

    @Column(name = "telephone_charges", precision = 10, scale = 2)
    private BigDecimal telephoneCharges = BigDecimal.ZERO;

    @Column(name = "spa_charges", precision = 10, scale = 2)
    private BigDecimal spaCharges = BigDecimal.ZERO;

    @Column(name = "gym_charges", precision = 10, scale = 2)
    private BigDecimal gymCharges = BigDecimal.ZERO;

    @Column(name = "business_center_charges", precision = 10, scale = 2)
    private BigDecimal businessCenterCharges = BigDecimal.ZERO;

    @Column(name = "other_service_charges", precision = 10, scale = 2)
    private BigDecimal otherServiceCharges = BigDecimal.ZERO;

    @Column(name = "total_service_charges", precision = 10, scale = 2)
    private BigDecimal totalServiceCharges = BigDecimal.ZERO;

    // Additional charges
    @Column(name = "late_checkout_charges", precision = 10, scale = 2)
    private BigDecimal lateCheckoutCharges = BigDecimal.ZERO;

    @Column(name = "early_checkout_penalty", precision = 10, scale = 2)
    private BigDecimal earlyCheckoutPenalty = BigDecimal.ZERO;

    @Column(name = "damage_charges", precision = 10, scale = 2)
    private BigDecimal damageCharges = BigDecimal.ZERO;

    @Column(name = "cleaning_charges", precision = 10, scale = 2)
    private BigDecimal cleaningCharges = BigDecimal.ZERO;

    @Column(name = "minibar_charges", precision = 10, scale = 2)
    private BigDecimal minibarCharges = BigDecimal.ZERO;

    @Column(name = "parking_charges", precision = 10, scale = 2)
    private BigDecimal parkingCharges = BigDecimal.ZERO;

    @Column(name = "total_additional_charges", precision = 10, scale = 2)
    private BigDecimal totalAdditionalCharges = BigDecimal.ZERO;

    // Tax calculations
    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "cgst_rate", precision = 5, scale = 2)
    private BigDecimal cgstRate = BigDecimal.ZERO;

    @Column(name = "cgst_amount", precision = 10, scale = 2)
    private BigDecimal cgstAmount = BigDecimal.ZERO;

    @Column(name = "sgst_rate", precision = 5, scale = 2)
    private BigDecimal sgstRate = BigDecimal.ZERO;

    @Column(name = "sgst_amount", precision = 10, scale = 2)
    private BigDecimal sgstAmount = BigDecimal.ZERO;

    @Column(name = "igst_rate", precision = 5, scale = 2)
    private BigDecimal igstRate = BigDecimal.ZERO;

    @Column(name = "igst_amount", precision = 10, scale = 2)
    private BigDecimal igstAmount = BigDecimal.ZERO;

    @Column(name = "total_tax_amount", precision = 10, scale = 2)
    private BigDecimal totalTaxAmount = BigDecimal.ZERO;

    // Discounts
    @Column(name = "discount_percentage", precision = 5, scale = 2)
    private BigDecimal discountPercentage = BigDecimal.ZERO;

    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "promotional_discount", precision = 10, scale = 2)
    private BigDecimal promotionalDiscount = BigDecimal.ZERO;

    @Column(name = "total_discount", precision = 10, scale = 2)
    private BigDecimal totalDiscount = BigDecimal.ZERO;

    // Final amounts
    @Column(name = "gross_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal grossTotal = BigDecimal.ZERO;

    @Column(name = "net_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal netAmount = BigDecimal.ZERO;

    @Column(name = "advance_paid", precision = 10, scale = 2)
    private BigDecimal advancePaid = BigDecimal.ZERO;

    @Column(name = "balance_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal balanceAmount = BigDecimal.ZERO;

    // Payment details
    @Column(name = "payment_mode", length = 50)
    private String paymentMode;

    @Column(name = "payment_reference", length = 100)
    private String paymentReference;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "payment_status", nullable = false, length = 20)
    private String paymentStatus = "PENDING";

    // Bill status and metadata
    @Column(name = "bill_status", nullable = false, length = 20)
    private String billStatus = "DRAFT";

    @Column(name = "bill_type", length = 30)
    private String billType = "CHECKOUT_BILL";

    @Column(name = "is_gst_applicable", nullable = false)
    private Boolean isGstApplicable = true;

    @Column(name = "printed_count", nullable = false)
    private Integer printedCount = 0;

    @Column(name = "email_sent", nullable = false)
    private Boolean emailSent = false;

    @Column(name = "remarks", length = 500)
    private String remarks;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "modified_by")
    private Long modifiedBy;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @Column(name = "audit_date", nullable = false)
    private LocalDate auditDate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
        if (this.billDate == null) {
            this.billDate = LocalDate.now();
        }
        if (this.auditDate == null) {
            this.auditDate = LocalDate.now();
        }

        // Auto-generate bill number if not provided
        if (this.billNo == null || this.billNo.isEmpty()) {
            this.billNo = "BILL_" + System.currentTimeMillis();
        }

        // Calculate all totals
        calculateTotals();
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedDate = LocalDateTime.now();
        calculateTotals();
    }

    // Helper method to calculate all totals
    public void calculateTotals() {
        // Calculate total room charges
        this.totalRoomCharges = this.roomRent.add(this.extraBedCharges);

        // Calculate total service charges
        this.totalServiceCharges = this.foodBeverageCharges
                .add(this.laundryCharges)
                .add(this.telephoneCharges)
                .add(this.spaCharges)
                .add(this.gymCharges)
                .add(this.businessCenterCharges)
                .add(this.otherServiceCharges);

        // Calculate total additional charges
        this.totalAdditionalCharges = this.lateCheckoutCharges
                .add(this.earlyCheckoutPenalty)
                .add(this.damageCharges)
                .add(this.cleaningCharges)
                .add(this.minibarCharges)
                .add(this.parkingCharges);

        // Calculate subtotal
        this.subtotal = this.totalRoomCharges
                .add(this.totalServiceCharges)
                .add(this.totalAdditionalCharges);

        // Calculate tax amounts
        if (this.isGstApplicable) {
            this.cgstAmount = this.subtotal.multiply(this.cgstRate).divide(BigDecimal.valueOf(100));
            this.sgstAmount = this.subtotal.multiply(this.sgstRate).divide(BigDecimal.valueOf(100));
            this.igstAmount = this.subtotal.multiply(this.igstRate).divide(BigDecimal.valueOf(100));
        }

        this.totalTaxAmount = this.cgstAmount.add(this.sgstAmount).add(this.igstAmount);

        // Calculate total discount
        this.totalDiscount = this.discountAmount.add(this.promotionalDiscount);

        // Calculate gross total
        this.grossTotal = this.subtotal.add(this.totalTaxAmount);

        // Calculate net amount
        this.netAmount = this.grossTotal.subtract(this.totalDiscount);

        // Calculate balance amount
        this.balanceAmount = this.netAmount.subtract(this.advancePaid);
    }
}
