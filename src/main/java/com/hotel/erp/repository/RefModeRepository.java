package com.hotel.erp.repository;

import com.hotel.erp.entity.RefMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RefModeRepository extends JpaRepository<RefMode, Integer> {
    Optional<RefMode> findByRefMode(String refMode);
}
