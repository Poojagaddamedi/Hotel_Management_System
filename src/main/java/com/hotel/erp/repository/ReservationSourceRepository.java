package com.hotel.erp.repository;

import com.hotel.erp.entity.ReservationSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ReservationSourceRepository extends JpaRepository<ReservationSource, Integer> {
    Optional<ReservationSource> findByResSource(String resSource);
}
