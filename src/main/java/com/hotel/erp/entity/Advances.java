package com.hotel.erp.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "advances")
public class Advances {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long advancesId;

    @Column(name = "reservation_no")
    private String reservationNo;

    @Column(name = "audit_date")
    private LocalDate auditDate;

    @Column(name = "adv_date")
    private LocalDate paymentDate;

    @Column(name = "receipt_no")
    private String referenceNo;

    @Column(name = "settlement_mode")
    private String paymentMode;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "credit_card_company")
    private String creditCardCompany;

    @Column(name = "credit_card_no")
    private String creditCardNo;

    @Column(name = "narration")
    private String remarks;

    @Column(name = "folio_no")
    private String folioNo;

    @Column(name = "shift_no")
    private Integer shiftNo;

    @Column(name = "shift_date")
    private LocalDate shiftDate;

    @Column(name = "room_no")
    private String roomNo;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "bill_no")
    private String billNo;

    @Column(name = "guest_name")
    private String guestName;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "updated_on")
    private LocalDateTime updatedOn;

    // Default constructor
    public Advances() {
    }

    // Getters and Setters
    public Long getAdvancesId() {
        return advancesId;
    }

    public void setAdvancesId(Long advancesId) {
        this.advancesId = advancesId;
    }

    public String getReservationNo() {
        return reservationNo;
    }

    public void setReservationNo(String reservationNo) {
        this.reservationNo = reservationNo;
    }

    public LocalDate getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(LocalDate auditDate) {
        this.auditDate = auditDate;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCreditCardCompany() {
        return creditCardCompany;
    }

    public void setCreditCardCompany(String creditCardCompany) {
        this.creditCardCompany = creditCardCompany;
    }

    public String getCreditCardNo() {
        return creditCardNo;
    }

    public void setCreditCardNo(String creditCardNo) {
        this.creditCardNo = creditCardNo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getFolioNo() {
        return folioNo;
    }

    public void setFolioNo(String folioNo) {
        this.folioNo = folioNo;
    }

    public Integer getShiftNo() {
        return shiftNo;
    }

    public void setShiftNo(Integer shiftNo) {
        this.shiftNo = shiftNo;
    }

    public LocalDate getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(LocalDate shiftDate) {
        this.shiftDate = shiftDate;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    @PrePersist
    protected void onCreate() {
        createdOn = LocalDateTime.now();
        updatedOn = LocalDateTime.now();
        if (auditDate == null) {
            auditDate = LocalDate.now();
        }
        if (paymentDate == null) {
            paymentDate = LocalDate.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedOn = LocalDateTime.now();
    }
} 