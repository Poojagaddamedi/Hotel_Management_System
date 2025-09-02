package com.hotel.erp.repository;

import com.hotel.erp.entity.BillSettlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BillSettlementRepository extends JpaRepository<BillSettlement, Long> {

    /**
     * Find settlements by bill ID
     * 
     * @param billId the bill ID
     * @return List of settlements for the given bill ID
     */
    List<BillSettlement> findByBillId(Long billId);

    /**
     * Find settlements by payment date
     * 
     * @param paymentDate the payment date
     * @return List of settlements for the given date
     */
    List<BillSettlement> findByPaymentDate(LocalDate paymentDate);

    /**
     * Find settlements by payment mode
     * 
     * @param paymentMode the payment mode
     * @return List of settlements for the given payment mode
     */
    List<BillSettlement> findByPaymentMode(String paymentMode);

    /**
     * Find settlements between dates
     * 
     * @param startDate the start date
     * @param endDate   the end date
     * @return List of settlements between the given dates
     */
    List<BillSettlement> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Custom query to search settlements by any field
     * 
     * @param query the search query
     * @return List of settlements matching the query
     */
    @Query("SELECT s FROM BillSettlement s WHERE " +
            "s.referenceNo LIKE %:query% OR " +
            "s.remarks LIKE %:query%")
    List<BillSettlement> searchSettlements(@Param("query") String query);

    /**
     * Get total amount by bill ID
     * 
     * @param billId the bill ID
     * @return total amount for the bill ID
     */
    @Query("SELECT COALESCE(SUM(s.amount), 0) FROM BillSettlement s WHERE s.billId = :billId")
    java.math.BigDecimal getTotalAmountByBillId(@Param("billId") Long billId);

    /**
     * Get total amount by payment mode
     * 
     * @param paymentMode the payment mode
     * @return total amount for the payment mode
     */
    @Query("SELECT COALESCE(SUM(s.amount), 0) FROM BillSettlement s WHERE s.paymentMode = :paymentMode")
    java.math.BigDecimal getTotalAmountByPaymentMode(@Param("paymentMode") String paymentMode);

    /**
     * Get total amount by date range
     * 
     * @param startDate the start date
     * @param endDate   the end date
     * @return total amount for the date range
     */
    @Query("SELECT COALESCE(SUM(s.amount), 0) FROM BillSettlement s WHERE s.paymentDate BETWEEN :startDate AND :endDate")
    java.math.BigDecimal getTotalAmountByDateRange(@Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    /**
     * Get total amount by date
     * 
     * @param date the date
     * @return total amount for the date
     */
    @Query("SELECT COALESCE(SUM(s.amount), 0) FROM BillSettlement s WHERE s.paymentDate = :date")
    java.math.BigDecimal getTotalAmountByDate(@Param("date") LocalDate date);

    /**
     * Count settlements by settlement date
     * 
     * @param settlementDate the settlement date
     * @return count of settlements for the date
     */
    int countByPaymentDate(LocalDate settlementDate);
}