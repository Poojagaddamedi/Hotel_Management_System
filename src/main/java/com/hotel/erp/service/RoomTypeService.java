package com.hotel.erp.service;

import com.hotel.erp.dto.RoomTypeDTO;
import java.util.List;

public interface RoomTypeService {
    RoomTypeDTO createRoomType(RoomTypeDTO roomTypeDTO);

    RoomTypeDTO updateRoomType(Integer id, RoomTypeDTO roomTypeDTO);

    RoomTypeDTO getRoomTypeById(Integer id);

    RoomTypeDTO getRoomTypeByName(String name);

    List<RoomTypeDTO> getAllRoomTypes();

    void deleteRoomType(Integer id);
}
