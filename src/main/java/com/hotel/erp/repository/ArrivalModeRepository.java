package com.hotel.erp.repository;

import com.hotel.erp.entity.ArrivalMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ArrivalModeRepository extends JpaRepository<ArrivalMode, Integer> {
    Optional<ArrivalMode> findByArrivalMode(String arrivalMode);
}
