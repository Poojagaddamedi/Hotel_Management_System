package com.hotel.erp.repository;

import com.hotel.erp.entity.Tax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxRepository extends JpaRepository<Tax, Integer> {
    // You can add custom query methods here if needed
}
