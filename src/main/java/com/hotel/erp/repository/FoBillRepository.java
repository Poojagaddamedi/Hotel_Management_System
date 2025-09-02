package com.hotel.erp.repository;

import com.hotel.erp.entity.FoBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FoBillRepository extends JpaRepository<FoBill, Long> {

    /**
     * Find bills by folio number
     * 
     * @param folioNo the folio number
     * @return List of bills for the given folio number
     */
    List<FoBill> findByFolioNo(String folioNo);

    /**
     * Find bills by bill date
     * 
     * @param billDate the bill date
     * @return List of bills for the given date
     */
    List<FoBill> findByBillDate(LocalDate billDate);

    /**
     * Find bills between dates
     * 
     * @param startDate the start date
     * @param endDate   the end date
     * @return List of bills between the given dates
     */
    List<FoBill> findByBillDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Custom query to search bills by any field
     * 
     * @param query the search query
     * @return List of bills matching the query
     */
    @Query("SELECT b FROM FoBill b WHERE " +
            "b.folioNo LIKE %:query%")
    List<FoBill> searchBills(@Param("query") String query);

    /**
     * Get total amount by folio number
     * 
     * @param folioNo the folio number
     * @return total amount for the folio number
     */
    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM FoBill b WHERE b.folioNo = :folioNo")
    java.math.BigDecimal getTotalAmountByFolioNo(@Param("folioNo") String folioNo);

    /**
     * Get total amount by date range
     * 
     * @param startDate the start date
     * @param endDate   the end date
     * @return total amount for the date range
     */
    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM FoBill b WHERE b.billDate BETWEEN :startDate AND :endDate")
    java.math.BigDecimal getTotalAmountByDateRange(@Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    /**
     * Get total amount by date
     * 
     * @param date the date
     * @return total amount for the date
     */
    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM FoBill b WHERE b.billDate = :date")
    java.math.BigDecimal getTotalAmountByDate(@Param("date") LocalDate date);

    /**
     * Count bills by bill date
     * 
     * @param billDate the bill date
     * @return count of bills for the date
     */
    int countByBillDate(LocalDate billDate);
}