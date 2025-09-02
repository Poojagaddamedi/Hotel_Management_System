package com.hotel.erp.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "post_transaction")
public class PostTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "room_no")
    private String roomNo;

    @Column(name = "audit_date")
    private LocalDate auditDate;

    @Column(name = "guest_name")
    private String guestName;

    @Column(name = "folio_no")
    private String folioNo;

    @Column(name = "trans_date")
    private LocalDate transDate;

    @Column(name = "acc_head")
    private String accHead;

    @Column(name = "voucher_no")
    private String voucherNo;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "narration")
    private String narration;

    @Column(name = "bill_no")
    private String billNo;

    @Column(name = "user_id")
    private Integer userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status")
    private TransactionStatus transactionStatus;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "updated_on")
    private LocalDateTime updatedOn;

    // Additional fields expected by controller
    @Column(name = "reservation_no")
    private String reservationNo;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "customer_type")
    private String customerType;

    @Column(name = "shift_no")
    private Integer shiftNo;

    @Column(name = "shift_date")
    private LocalDate shiftDate;

    // Default constructor
    public PostTransaction() {
    }

    // Constructor with required fields
    public PostTransaction(String folioNo, String guestName, String accHead, BigDecimal amount, String narration,
            Integer userId) {
        this.folioNo = folioNo;
        this.guestName = guestName;
        this.accHead = accHead;
        this.amount = amount;
        this.narration = narration;
        this.userId = userId;
        this.transDate = LocalDate.now();
        this.auditDate = LocalDate.now();
        this.transactionStatus = TransactionStatus.Pending;
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

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
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

    @PrePersist
    protected void onCreate() {
        createdOn = LocalDateTime.now();
        updatedOn = LocalDateTime.now();
        if (auditDate == null) {
            auditDate = LocalDate.now();
        }
        if (transDate == null) {
            transDate = LocalDate.now();
        }
        if (transactionStatus == null) {
            transactionStatus = TransactionStatus.Pending;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedOn = LocalDateTime.now();
    }

    // Transaction Status Enum
    public enum TransactionStatus {
        Pending,
        Completed,
        Failed
    }

    @Override
    public String toString() {
        return "PostTransaction{" +
                "id=" + id +
                ", folioNo='" + folioNo + '\'' +
                ", guestName='" + guestName + '\'' +
                ", accHead='" + accHead + '\'' +
                ", amount=" + amount +
                ", transDate=" + transDate +
                ", transactionStatus=" + transactionStatus +
                '}';
    }
}