package com.hotel.erp.repository;

import com.hotel.erp.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Integer> {
    Optional<RoomType> findByRoomTypeName(String name);

    boolean existsByRoomTypeName(String name);
}
