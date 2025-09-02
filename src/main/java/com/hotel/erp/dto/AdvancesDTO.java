package com.hotel.erp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AdvancesDTO {
    private Long advancesId;
    
    private String reservationNo;
    
    private LocalDate auditDate;
    
    private LocalDate paymentDate;
    
    private String referenceNo;
    
    private String paymentMode;
    
    private BigDecimal amount;
    
    private String creditCardCompany;
    
    private String creditCardNo;
    
    private String remarks;
    
    private String folioNo;
    
    private Integer shiftNo;
    
    private LocalDate shiftDate;
    
    private String roomNo;
    
    private Integer userId;
    
    private String billNo;
    
    private String guestName;

    // Default constructor
    public AdvancesDTO() {
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
} 