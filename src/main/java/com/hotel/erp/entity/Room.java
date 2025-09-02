package com.hotel.erp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "room")
@Data
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "floor")
    private Integer floor;

    @Column(name = "room_no", unique = true)
    private String roomNo;

    @Column(name = "no_of_persons")
    private Integer noOfPersons;

    @Column(name = "picture")
    private String picture;

    @Column(name = "room_discription")
    private String roomDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RoomStatus status;

    @Column(name = "rate")
    private Double rate;

    @Column(name = "current_folio")
    private String currentFolio;

    @Column(name = "roomtype_id")
    private Integer roomTypeId;

    public enum RoomStatus {
        VR, // Vacant and Ready
        OD, // Occupied Dirty
        OI // Occupied and Inspected
    }

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
}
