package com.hotel.erp.service;

import com.hotel.erp.dto.RoomTypeDTO;
import com.hotel.erp.entity.RoomType;
import com.hotel.erp.repository.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomTypeServiceImpl implements RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;

    @Autowired
    public RoomTypeServiceImpl(RoomTypeRepository roomTypeRepository) {
        this.roomTypeRepository = roomTypeRepository;
    }

    @Override
    public RoomTypeDTO createRoomType(RoomTypeDTO roomTypeDTO) {
        // Check if room type with same name already exists
        if (roomTypeRepository.existsByRoomTypeName(roomTypeDTO.getRoomTypeName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Room type with name " + roomTypeDTO.getRoomTypeName() + " already exists");
        }

        // Convert DTO to entity
        RoomType roomType = mapToEntity(roomTypeDTO);

        // Save entity
        RoomType savedRoomType = roomTypeRepository.save(roomType);

        // Convert back to DTO and return
        return mapToDTO(savedRoomType);
    }

    @Override
    public RoomTypeDTO updateRoomType(Integer id, RoomTypeDTO roomTypeDTO) {
        // Find existing room type
        RoomType roomType = roomTypeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Room type with id " + id + " not found"));

        // Check if new name conflicts with existing (but not this room type)
        if (!roomType.getRoomTypeName().equals(roomTypeDTO.getRoomTypeName()) &&
                roomTypeRepository.existsByRoomTypeName(roomTypeDTO.getRoomTypeName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Room type with name " + roomTypeDTO.getRoomTypeName() + " already exists");
        }

        // Update fields
        roomType.setRoomTypeName(roomTypeDTO.getRoomTypeName());
        roomType.setRoomTypeDescription(roomTypeDTO.getRoomTypeDescription());
        roomType.setRoomTypeRate(roomTypeDTO.getRoomTypeRate());

        // Save changes
        RoomType updatedRoomType = roomTypeRepository.save(roomType);

        // Return updated DTO
        return mapToDTO(updatedRoomType);
    }

    @Override
    public RoomTypeDTO getRoomTypeById(Integer id) {
        return roomTypeRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Room type with id " + id + " not found"));
    }

    @Override
    public RoomTypeDTO getRoomTypeByName(String name) {
        return roomTypeRepository.findByRoomTypeName(name)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Room type with name " + name + " not found"));
    }

    @Override
    public List<RoomTypeDTO> getAllRoomTypes() {
        return roomTypeRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRoomType(Integer id) {
        if (!roomTypeRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Room type with id " + id + " not found");
        }

        roomTypeRepository.deleteById(id);
    }

    // Helper method to map entity to DTO
    private RoomTypeDTO mapToDTO(RoomType roomType) {
        RoomTypeDTO dto = new RoomTypeDTO();
        dto.setRoomTypeId(roomType.getRoomTypeId());
        dto.setRoomTypeName(roomType.getRoomTypeName());
        dto.setRoomTypeDescription(roomType.getRoomTypeDescription());
        dto.setRoomTypeRate(roomType.getRoomTypeRate());
        return dto;
    }

    // Helper method to map DTO to entity
    private RoomType mapToEntity(RoomTypeDTO dto) {
        RoomType entity = new RoomType();

        // Don't set ID for new entities
        if (dto.getRoomTypeId() != null) {
            entity.setRoomTypeId(dto.getRoomTypeId());
        }

        entity.setRoomTypeName(dto.getRoomTypeName());
        entity.setRoomTypeDescription(dto.getRoomTypeDescription());
        entity.setRoomTypeRate(dto.getRoomTypeRate());
        return entity;
    }
}
