package com.hotel.erp.repository;

import com.hotel.erp.entity.Nationality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface NationalityRepository extends JpaRepository<Nationality, Integer> {
    Optional<Nationality> findByCountryName(String countryName);

    Optional<Nationality> findByCountryCode(String countryCode);

    boolean existsByCountryName(String countryName);
}
