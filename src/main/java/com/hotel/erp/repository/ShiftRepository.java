package com.hotel.erp.repository;

import com.hotel.erp.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {
    Optional<Shift> findByShiftNumber(Integer shiftNumber);

    Optional<Shift> findTopByOrderByShiftNumberDesc();
}
