package com.hotel.erp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PostTransactionDTO {

    private Long id;
    private String roomNo;
    private LocalDate auditDate;
    private String guestName;
    private String folioNo;
    private LocalDate transDate;
    private String accHead;
    private String voucherNo;
    private BigDecimal amount;
    private String narration;
    private String billNo;
    private Integer userId;
    private String transactionStatus;

    // Additional fields expected by controller
    private String reservationNo;
    private String transactionType;
    private String customerType;
    private Integer shiftNo;
    private LocalDate shiftDate;

    // Default constructor
    public PostTransactionDTO() {
    }

    // Constructor with required fields
    public PostTransactionDTO(String folioNo, String guestName, String accHead, BigDecimal amount, String narration,
            Integer userId) {
        this.folioNo = folioNo;
        this.guestName = guestName;
        this.accHead = accHead;
        this.amount = amount;
        this.narration = narration;
        this.userId = userId;
        this.transDate = LocalDate.now();
        this.auditDate = LocalDate.now();
        this.transactionStatus = "Pending";
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public LocalDate getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(LocalDate auditDate) {
        this.auditDate = auditDate;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getFolioNo() {
        return folioNo;
    }

    public void setFolioNo(String folioNo) {
        this.folioNo = folioNo;
    }

    public LocalDate getTransDate() {
        return transDate;
    }

    public void setTransDate(LocalDate transDate) {
        this.transDate = transDate;
    }

    public String getAccHead() {
        return accHead;
    }

    public void setAccHead(String accHead) {
        this.accHead = accHead;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    // Additional getters and setters for new fields
    public String getReservationNo() {
        return reservationNo;
    }

    public void setReservationNo(String reservationNo) {
        this.reservationNo = reservationNo;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
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

    @Override
    public String toString() {
        return "PostTransactionDTO{" +
                "id=" + id +
                ", folioNo='" + folioNo + '\'' +
                ", guestName='" + guestName + '\'' +
                ", accHead='" + accHead + '\'' +
                ", amount=" + amount +
                ", transDate=" + transDate +
                ", transactionStatus='" + transactionStatus + '\'' +
                '}';
    }
}