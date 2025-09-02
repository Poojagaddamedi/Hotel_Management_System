package com.hotel.erp.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "reservation_no")
    private String reservationNo;

    @Column(name = "from_date", nullable = false)
    private LocalDate fromDate;

    @Column(name = "to_date", nullable = false)
    private LocalDate toDate;

    @Column(name = "audit_date")
    private LocalDate auditDate;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "no_of_rooms", nullable = false)
    private Integer noOfRooms = 1;

    @Column(name = "contact_no", nullable = false)
    private String contactNo;

    @Column(name = "email_id")
    private String emailId;

    @Column(name = "guest_name", nullable = false)
    private String guestName;

    @Column(name = "total_pax", nullable = false)
    private Integer totalPax;

    @Column(name = "rate")
    private Double rate;

    @Column(name = "tax")
    private Double tax;

    @Column(name = "with_tax")
    private String withTax = "Y";

    @Column(name = "is_tax_inclusive")
    private Boolean isTaxInclusive = false;

    @Column(name = "total_amt")
    private Double totalAmt;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "purpose")
    private String purpose;

    @Column(name = "status")
    private String status = "0"; // 0 - Booked, 1 - Cancelled

    @Column(name = "selected_room")
    private String selectedRoom;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "updated_on")
    private LocalDateTime updatedOn;

    @Column(name = "is_checkin_done")
    private Boolean isCheckinDone;

    @Column(name = "nationality")
    private Integer nationality;

    @Column(name = "external_ref_no")
    private String externalRefNo;

    @Column(name = "rooms_check_in")
    private Integer roomsCheckIn;

    @Column(name = "user_id")
    private Integer userId;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReservationNo() {
        return reservationNo;
    }

    public void setReservationNo(String reservationNo) {
        this.reservationNo = reservationNo;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public LocalDate getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(LocalDate auditDate) {
        this.auditDate = auditDate;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getNoOfRooms() {
        return noOfRooms;
    }

    public void setNoOfRooms(Integer noOfRooms) {
        this.noOfRooms = noOfRooms;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public Integer getTotalPax() {
        return totalPax;
    }

    public void setTotalPax(Integer totalPax) {
        this.totalPax = totalPax;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public String getWithTax() {
        return withTax;
    }

    public void setWithTax(String withTax) {
        this.withTax = withTax;
    }

    public Boolean getIsTaxInclusive() {
        return isTaxInclusive;
    }

    public void setIsTaxInclusive(Boolean isTaxInclusive) {
        this.isTaxInclusive = isTaxInclusive;
    }

    public Double getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(Double totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSelectedRoom() {
        return selectedRoom;
    }

    public void setSelectedRoom(String selectedRoom) {
        this.selectedRoom = selectedRoom;
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

    public Boolean getIsCheckinDone() {
        return isCheckinDone;
    }

    public void setIsCheckinDone(Boolean isCheckinDone) {
        this.isCheckinDone = isCheckinDone;
    }

    public Integer getNationality() {
        return nationality;
    }

    public void setNationality(Integer nationality) {
        this.nationality = nationality;
    }

    public String getExternalRefNo() {
        return externalRefNo;
    }

    public void setExternalRefNo(String externalRefNo) {
        this.externalRefNo = externalRefNo;
    }

    public Integer getRoomsCheckIn() {
        return roomsCheckIn;
    }

    public void setRoomsCheckIn(Integer roomsCheckIn) {
        this.roomsCheckIn = roomsCheckIn;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
