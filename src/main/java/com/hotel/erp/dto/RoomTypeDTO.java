package com.hotel.erp.dto;

import lombok.Data;

@Data
public class RoomTypeDTO {
    private Integer roomTypeId;
    private String roomTypeName;
    private String roomTypeDescription;
    private Double roomTypeRate;

    // Explicit getters/setters for compilation compatibility
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

    public String getRoomTypeDescription() {
        return roomTypeDescription;
    }

    public void setRoomTypeDescription(String roomTypeDescription) {
        this.roomTypeDescription = roomTypeDescription;
    }

    public Double getRoomTypeRate() {
        return roomTypeRate;
    }

    public void setRoomTypeRate(Double roomTypeRate) {
        this.roomTypeRate = roomTypeRate;
    }
}
