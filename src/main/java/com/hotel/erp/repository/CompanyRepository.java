package com.hotel.erp.repository;

import com.hotel.erp.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    Optional<Company> findByCompanyName(String name);

    boolean existsByCompanyName(String name);

    List<Company> findByGst(String gst);
}
