package com.hotel.erp.repository;

import com.hotel.erp.entity.PaymentMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PaymentModeRepository extends JpaRepository<PaymentMode, Integer> {
    Optional<PaymentMode> findBySettlementType(String type);

    boolean existsBySettlementType(String type);
}
