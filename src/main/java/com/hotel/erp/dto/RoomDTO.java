package com.hotel.erp.dto;

import lombok.Data;
import com.hotel.erp.entity.Room.RoomStatus;

@Data
public class RoomDTO {
    private Integer id;
    private Integer floor;
    private String roomNo;
    private Integer noOfPersons;
    private String picture;
    private String roomDescription;
    private RoomStatus status;
    private Double rate;
    private String currentFolio;
    private Integer roomTypeId;
    private String roomTypeName; // Added for convenience

    // Manual getters and setters for fields that are causing compilation errors
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public Integer getNoOfPersons() {
        return noOfPersons;
    }

    public void setNoOfPersons(Integer noOfPersons) {
        this.noOfPersons = noOfPersons;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getCurrentFolio() {
        return currentFolio;
    }

    public void setCurrentFolio(String currentFolio) {
        this.currentFolio = currentFolio;
    }

    public Integer getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Integer roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }
}
