package com.hotel.erp.repository;

import com.hotel.erp.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    Optional<Room> findByRoomNo(String roomNo);

    List<Room> findByFloor(Integer floor);

    List<Room> findByStatus(Room.RoomStatus status);

    List<Room> findByRoomTypeId(Integer roomTypeId);

    boolean existsByRoomNo(String roomNo);
}
