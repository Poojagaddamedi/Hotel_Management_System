package com.hotel.erp.repository;

import com.hotel.erp.entity.AccountYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountYearRepository extends JpaRepository<AccountYear, String> {
    Optional<AccountYear> findTopByOrderByAccYearDesc();
}
