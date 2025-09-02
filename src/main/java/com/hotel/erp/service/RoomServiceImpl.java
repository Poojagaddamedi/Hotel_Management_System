package com.hotel.erp.service;

import com.hotel.erp.dto.RoomDTO;
import com.hotel.erp.entity.Room;
import com.hotel.erp.entity.Room.RoomStatus;
import com.hotel.erp.entity.RoomType;
import com.hotel.erp.repository.RoomRepository;
import com.hotel.erp.repository.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository, RoomTypeRepository roomTypeRepository) {
        this.roomRepository = roomRepository;
        this.roomTypeRepository = roomTypeRepository;
    }

    @Override
    public RoomDTO createRoom(RoomDTO roomDTO) {
        // Check if room with same number already exists
        if (roomRepository.existsByRoomNo(roomDTO.getRoomNo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Room with number " + roomDTO.getRoomNo() + " already exists");
        }

        // Validate the room type exists
        if (roomDTO.getRoomTypeId() != null) {
            validateRoomTypeExists(roomDTO.getRoomTypeId());
        }

        // Convert DTO to entity
        Room room = mapToEntity(roomDTO);

        // Save entity
        Room savedRoom = roomRepository.save(room);

        // Convert back to DTO and return
        return mapToDTO(savedRoom);
    }

    @Override
    public RoomDTO updateRoom(Integer id, RoomDTO roomDTO) {
        // Find existing room
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Room with id " + id + " not found"));

        // Check if new room number conflicts with existing (but not this room)
        if (!room.getRoomNo().equals(roomDTO.getRoomNo()) &&
                roomRepository.existsByRoomNo(roomDTO.getRoomNo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Room with number " + roomDTO.getRoomNo() + " already exists");
        }

        // Validate the room type exists
        if (roomDTO.getRoomTypeId() != null) {
            validateRoomTypeExists(roomDTO.getRoomTypeId());
        }

        // Update fields
        room.setRoomNo(roomDTO.getRoomNo());
        room.setFloor(roomDTO.getFloor());
        room.setNoOfPersons(roomDTO.getNoOfPersons());
        room.setPicture(roomDTO.getPicture());
        room.setRoomDescription(roomDTO.getRoomDescription());
        room.setStatus(roomDTO.getStatus());
        room.setRate(roomDTO.getRate());
        room.setCurrentFolio(roomDTO.getCurrentFolio());
        room.setRoomTypeId(roomDTO.getRoomTypeId());

        // Save changes
        Room updatedRoom = roomRepository.save(room);

        // Return updated DTO
        return mapToDTO(updatedRoom);
    }

    @Override
    public RoomDTO getRoomById(Integer id) {
        return roomRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Room with id " + id + " not found"));
    }

    @Override
    public RoomDTO getRoomByRoomNo(String roomNo) {
        return roomRepository.findByRoomNo(roomNo)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Room with number " + roomNo + " not found"));
    }

    @Override
    public List<RoomDTO> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomDTO> getRoomsByFloor(Integer floor) {
        return roomRepository.findByFloor(floor).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomDTO> getRoomsByStatus(RoomStatus status) {
        return roomRepository.findByStatus(status).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomDTO> getRoomsByRoomType(Integer roomTypeId) {
        // Validate the room type exists
        validateRoomTypeExists(roomTypeId);

        return roomRepository.findByRoomTypeId(roomTypeId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRoom(Integer id) {
        if (!roomRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Room with id " + id + " not found");
        }

        roomRepository.deleteById(id);
    }

    // Helper method to validate room type exists
    private void validateRoomTypeExists(Integer roomTypeId) {
        if (!roomTypeRepository.existsById(roomTypeId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Room type with id " + roomTypeId + " does not exist");
        }
    }

    // Helper method to map entity to DTO
    private RoomDTO mapToDTO(Room room) {
        RoomDTO dto = new RoomDTO();
        dto.setId(room.getId());
        dto.setRoomNo(room.getRoomNo());
        dto.setFloor(room.getFloor());
        dto.setNoOfPersons(room.getNoOfPersons());
        dto.setPicture(room.getPicture());
        dto.setRoomDescription(room.getRoomDescription());
        dto.setStatus(room.getStatus());
        dto.setRate(room.getRate());
        dto.setCurrentFolio(room.getCurrentFolio());
        dto.setRoomTypeId(room.getRoomTypeId());

        // Set room type name if available
        if (room.getRoomTypeId() != null) {
            Optional<RoomType> roomType = roomTypeRepository.findById(room.getRoomTypeId());
            roomType.ifPresent(type -> dto.setRoomTypeName(type.getRoomTypeName()));
        }

        return dto;
    }

    // Helper method to map DTO to entity
    private Room mapToEntity(RoomDTO dto) {
        Room room = new Room();

        // Don't set ID for new entities
        if (dto.getId() != null) {
            room.setId(dto.getId());
        }

        room.setRoomNo(dto.getRoomNo());
        room.setFloor(dto.getFloor());
        room.setNoOfPersons(dto.getNoOfPersons());
        room.setPicture(dto.getPicture());
        room.setRoomDescription(dto.getRoomDescription());
        room.setStatus(dto.getStatus());
        room.setRate(dto.getRate());
        room.setCurrentFolio(dto.getCurrentFolio());
        room.setRoomTypeId(dto.getRoomTypeId());

        return room;
    }
}
