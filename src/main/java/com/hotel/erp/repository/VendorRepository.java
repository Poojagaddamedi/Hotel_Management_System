package com.hotel.erp.repository;

import com.hotel.erp.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {

    Optional<Vendor> findByVendorCode(String vendorCode);

    List<Vendor> findByVendorName(String vendorName);

    List<Vendor> findByVendorType(String vendorType);

    List<Vendor> findByCategory(String category);

    List<Vendor> findByStatus(String status);

    List<Vendor> findByCity(String city);

    List<Vendor> findByState(String state);

    List<Vendor> findByCountry(String country);

    List<Vendor> findByPaymentTerms(String paymentTerms);

    List<Vendor> findByRating(Integer rating);

    List<Vendor> findByContactPerson(String contactPerson);

    @Query("SELECT v FROM Vendor v WHERE v.vendorType = :type AND v.category = :category")
    List<Vendor> findByTypeAndCategory(@Param("type") String type, @Param("category") String category);

    @Query("SELECT v FROM Vendor v WHERE v.status = :status AND v.category = :category")
    List<Vendor> findByStatusAndCategory(@Param("status") String status, @Param("category") String category);

    @Query("SELECT v FROM Vendor v WHERE v.contractEndDate <= :date AND v.status = 'ACTIVE'")
    List<Vendor> findExpiringContracts(@Param("date") LocalDate date);

    @Query("SELECT v FROM Vendor v WHERE v.creditLimit > 0 AND v.status = 'ACTIVE'")
    List<Vendor> findVendorsWithCreditLimit();

    @Query("SELECT v FROM Vendor v WHERE v.rating >= :minRating AND v.status = 'ACTIVE'")
    List<Vendor> findByMinimumRating(@Param("minRating") Integer minRating);

    @Query("SELECT COUNT(v) FROM Vendor v WHERE v.status = :status")
    Long countByStatus(@Param("status") String status);

    @Query("SELECT COUNT(v) FROM Vendor v WHERE v.category = :category AND v.status = 'ACTIVE'")
    Long countByCategoryAndActiveStatus(@Param("category") String category);

    @Query("SELECT v FROM Vendor v WHERE v.vendorName LIKE %:query% OR v.contactPerson LIKE %:query% OR v.email LIKE %:query% OR v.phone LIKE %:query%")
    List<Vendor> searchVendors(@Param("query") String query);

    @Query("SELECT v FROM Vendor v WHERE v.auditDate BETWEEN :startDate AND :endDate")
    List<Vendor> findByAuditDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT DISTINCT v.category FROM Vendor v WHERE v.status = 'ACTIVE'")
    List<String> findActiveCategories();

    @Query("SELECT DISTINCT v.vendorType FROM Vendor v WHERE v.status = 'ACTIVE'")
    List<String> findActiveVendorTypes();
}
