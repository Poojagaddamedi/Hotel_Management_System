package com.hotel.erp.controller;

import com.hotel.erp.dto.RoomDTO;
import com.hotel.erp.entity.Room.RoomStatus;
import com.hotel.erp.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/rooms")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public ResponseEntity<List<RoomDTO>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDTO> getRoomById(@PathVariable Integer id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    @GetMapping("/number/{roomNo}")
    public ResponseEntity<RoomDTO> getRoomByRoomNo(@PathVariable String roomNo) {
        return ResponseEntity.ok(roomService.getRoomByRoomNo(roomNo));
    }

    @GetMapping("/floor/{floor}")
    public ResponseEntity<List<RoomDTO>> getRoomsByFloor(@PathVariable Integer floor) {
        return ResponseEntity.ok(roomService.getRoomsByFloor(floor));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<RoomDTO>> getRoomsByStatus(@PathVariable RoomStatus status) {
        return ResponseEntity.ok(roomService.getRoomsByStatus(status));
    }

    @GetMapping("/room-type/{roomTypeId}")
    public ResponseEntity<List<RoomDTO>> getRoomsByRoomType(@PathVariable Integer roomTypeId) {
        return ResponseEntity.ok(roomService.getRoomsByRoomType(roomTypeId));
    }

    @PostMapping
    public ResponseEntity<RoomDTO> createRoom(@RequestBody RoomDTO roomDTO) {
        return new ResponseEntity<>(roomService.createRoom(roomDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomDTO> updateRoom(@PathVariable Integer id, @RequestBody RoomDTO roomDTO) {
        return ResponseEntity.ok(roomService.updateRoom(id, roomDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Integer id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}
