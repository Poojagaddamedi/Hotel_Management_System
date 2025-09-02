package com.hotel.erp.service;

import com.hotel.erp.dto.RoomDTO;
import com.hotel.erp.entity.Room.RoomStatus;
import java.util.List;

public interface RoomService {
    RoomDTO createRoom(RoomDTO roomDTO);

    RoomDTO updateRoom(Integer id, RoomDTO roomDTO);

    RoomDTO getRoomById(Integer id);

    RoomDTO getRoomByRoomNo(String roomNo);

    List<RoomDTO> getAllRooms();

    List<RoomDTO> getRoomsByFloor(Integer floor);

    List<RoomDTO> getRoomsByStatus(RoomStatus status);

    List<RoomDTO> getRoomsByRoomType(Integer roomTypeId);

    void deleteRoom(Integer id);
}
