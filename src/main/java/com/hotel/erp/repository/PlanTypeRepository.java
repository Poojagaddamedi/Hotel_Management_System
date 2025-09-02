package com.hotel.erp.repository;

import com.hotel.erp.entity.PlanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PlanTypeRepository extends JpaRepository<PlanType, Integer> {
    Optional<PlanType> findByPlanName(String name);

    boolean existsByPlanName(String name);
}
