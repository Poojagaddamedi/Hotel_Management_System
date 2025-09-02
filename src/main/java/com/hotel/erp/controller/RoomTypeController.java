package com.hotel.erp.controller;

import com.hotel.erp.dto.RoomTypeDTO;
import com.hotel.erp.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/room-types")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    @Autowired
    public RoomTypeController(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

    @GetMapping
    public ResponseEntity<List<RoomTypeDTO>> getAllRoomTypes() {
        return ResponseEntity.ok(roomTypeService.getAllRoomTypes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomTypeDTO> getRoomTypeById(@PathVariable Integer id) {
        return ResponseEntity.ok(roomTypeService.getRoomTypeById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<RoomTypeDTO> getRoomTypeByName(@PathVariable String name) {
        return ResponseEntity.ok(roomTypeService.getRoomTypeByName(name));
    }

    @PostMapping
    public ResponseEntity<RoomTypeDTO> createRoomType(@RequestBody RoomTypeDTO roomTypeDTO) {
        return new ResponseEntity<>(roomTypeService.createRoomType(roomTypeDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomTypeDTO> updateRoomType(@PathVariable Integer id, @RequestBody RoomTypeDTO roomTypeDTO) {
        return ResponseEntity.ok(roomTypeService.updateRoomType(id, roomTypeDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomType(@PathVariable Integer id) {
        roomTypeService.deleteRoomType(id);
        return ResponseEntity.noContent().build();
    }
}
