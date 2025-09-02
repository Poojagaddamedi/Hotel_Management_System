package com.hotel.erp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "roomtype")
@Data
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roomtype_id")
    private Integer roomTypeId;

    @Column(name = "roomtype_name", nullable = false)
    private String roomTypeName;

    @Column(name = "roomtype_description")
    private String roomTypeDescription;

    @Column(name = "roomtype_rate")
    private Double roomTypeRate;

    // Explicit getters for compilation compatibility
    public Integer getRoomTypeId() {
        return roomTypeId;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public String getRoomTypeDescription() {
        return roomTypeDescription;
    }

    public Double getRoomTypeRate() {
        return roomTypeRate;
    }

    public void setRoomTypeId(Integer roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public void setRoomTypeDescription(String roomTypeDescription) {
        this.roomTypeDescription = roomTypeDescription;
    }

    public void setRoomTypeRate(Double roomTypeRate) {
        this.roomTypeRate = roomTypeRate;
    }
}
