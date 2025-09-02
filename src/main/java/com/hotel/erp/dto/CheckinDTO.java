package com.hotel.erp.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CheckinDTO {
    private Long id;
    private String reservationNo;
    private String roomNo;
    private LocalDate checkInDate;
    private LocalDate expectedCheckOutDate;
    private LocalDate actualCheckOutDate;
    private String guestName;
    private Integer totalPax;
    private String folioNo;
    private String remarks;
    private Integer userId;
    private String status;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    // Default constructor
    public CheckinDTO() {
    }

    // Constructor with essential fields
    public CheckinDTO(String reservationNo, String roomNo, LocalDate checkInDate, String guestName, String folioNo) {
        this.reservationNo = reservationNo;
        this.roomNo = roomNo;
        this.checkInDate = checkInDate;
        this.guestName = guestName;
        this.folioNo = folioNo;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReservationNo() {
        return reservationNo;
    }

    public void setReservationNo(String reservationNo) {
        this.reservationNo = reservationNo;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getExpectedCheckOutDate() {
        return expectedCheckOutDate;
    }

    public void setExpectedCheckOutDate(LocalDate expectedCheckOutDate) {
        this.expectedCheckOutDate = expectedCheckOutDate;
    }

    public LocalDate getActualCheckOutDate() {
        return actualCheckOutDate;
    }

    public void setActualCheckOutDate(LocalDate actualCheckOutDate) {
        this.actualCheckOutDate = actualCheckOutDate;
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

    public String getFolioNo() {
        return folioNo;
    }

    public void setFolioNo(String folioNo) {
        this.folioNo = folioNo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    @Override
    public String toString() {
        return "CheckinDTO{" +
                "id=" + id +
                ", reservationNo='" + reservationNo + '\'' +
                ", roomNo='" + roomNo + '\'' +
                ", checkInDate=" + checkInDate +
                ", expectedCheckOutDate=" + expectedCheckOutDate +
                ", guestName='" + guestName + '\'' +
                ", folioNo='" + folioNo + '\'' +
                '}';
    }
}
