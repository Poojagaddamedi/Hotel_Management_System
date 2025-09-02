package com.hotel.erp.repository;

import com.hotel.erp.entity.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {

    // Find by bill number
    Optional<Billing> findByBillNo(String billNo);

    // Find by folio number
    Optional<Billing> findByFolioNo(String folioNo);

    // Find by reservation number
    Optional<Billing> findByReservationNo(String reservationNo);

    // Find by guest name
    List<Billing> findByGuestNameContainingIgnoreCase(String guestName);

    // Find by room number
    List<Billing> findByRoomNo(String roomNo);

    // Find by company name
    List<Billing> findByCompanyNameContainingIgnoreCase(String companyName);

    // Find by bill date range
    List<Billing> findByBillDateBetween(LocalDate startDate, LocalDate endDate);

    // Find by bill status
    List<Billing> findByBillStatus(String billStatus);

    // Find by payment status
    List<Billing> findByPaymentStatus(String paymentStatus);

    // Find by bill type
    List<Billing> findByBillType(String billType);

    // Find by created by
    List<Billing> findByCreatedBy(Long createdBy);

    // Find pending bills
    @Query("SELECT b FROM Billing b WHERE b.paymentStatus = 'PENDING' AND b.billStatus = 'FINALIZED'")
    List<Billing> findPendingBills();

    // Find overdue bills (older than specified days)
    @Query("SELECT b FROM Billing b WHERE b.paymentStatus = 'PENDING' AND b.billDate < :cutoffDate")
    List<Billing> findOverdueBills(@Param("cutoffDate") LocalDate cutoffDate);

    // Find bills with outstanding balance
    @Query("SELECT b FROM Billing b WHERE b.balanceAmount > 0")
    List<Billing> findBillsWithOutstandingBalance();

    // Get total revenue for date range
    @Query("SELECT COALESCE(SUM(b.netAmount), 0) FROM Billing b WHERE b.billDate BETWEEN :startDate AND :endDate AND b.paymentStatus = 'PAID'")
    BigDecimal getTotalRevenueByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // Get total outstanding amount
    @Query("SELECT COALESCE(SUM(b.balanceAmount), 0) FROM Billing b WHERE b.balanceAmount > 0")
    BigDecimal getTotalOutstandingAmount();

    // Get total tax collected for date range
    @Query("SELECT COALESCE(SUM(b.totalTaxAmount), 0) FROM Billing b WHERE b.billDate BETWEEN :startDate AND :endDate AND b.paymentStatus = 'PAID'")
    BigDecimal getTotalTaxCollectedByDateRange(@Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // Get bill count by status
    @Query("SELECT COUNT(b) FROM Billing b WHERE b.billStatus = :status")
    Long countByBillStatus(@Param("status") String status);

    // Get payment count by status
    @Query("SELECT COUNT(b) FROM Billing b WHERE b.paymentStatus = :status")
    Long countByPaymentStatus(@Param("status") String status);

    // Get average bill amount
    @Query("SELECT AVG(b.netAmount) FROM Billing b WHERE b.billStatus = 'FINALIZED'")
    Double getAverageBillAmount();

    // Find bills by date range and payment status
    @Query("SELECT b FROM Billing b WHERE b.billDate BETWEEN :startDate AND :endDate AND b.paymentStatus = :paymentStatus")
    List<Billing> findByDateRangeAndPaymentStatus(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("paymentStatus") String paymentStatus);

    // Advanced search with multiple filters
    @Query("SELECT b FROM Billing b WHERE " +
            "(:billNo IS NULL OR b.billNo = :billNo) AND " +
            "(:folioNo IS NULL OR b.folioNo = :folioNo) AND " +
            "(:guestName IS NULL OR LOWER(b.guestName) LIKE LOWER(CONCAT('%', :guestName, '%'))) AND " +
            "(:roomNo IS NULL OR b.roomNo = :roomNo) AND " +
            "(:companyName IS NULL OR LOWER(b.companyName) LIKE LOWER(CONCAT('%', :companyName, '%'))) AND " +
            "(:startDate IS NULL OR b.billDate >= :startDate) AND " +
            "(:endDate IS NULL OR b.billDate <= :endDate) AND " +
            "(:billStatus IS NULL OR b.billStatus = :billStatus) AND " +
            "(:paymentStatus IS NULL OR b.paymentStatus = :paymentStatus)")
    List<Billing> findByMultipleFilters(
            @Param("billNo") String billNo,
            @Param("folioNo") String folioNo,
            @Param("guestName") String guestName,
            @Param("roomNo") String roomNo,
            @Param("companyName") String companyName,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("billStatus") String billStatus,
            @Param("paymentStatus") String paymentStatus);

    // Get revenue breakdown by service type
    @Query("SELECT " +
            "COALESCE(SUM(b.totalRoomCharges), 0) as roomRevenue, " +
            "COALESCE(SUM(b.foodBeverageCharges), 0) as fbRevenue, " +
            "COALESCE(SUM(b.laundryCharges), 0) as laundryRevenue, " +
            "COALESCE(SUM(b.spaCharges), 0) as spaRevenue, " +
            "COALESCE(SUM(b.totalServiceCharges), 0) as totalServiceRevenue, " +
            "COALESCE(SUM(b.totalAdditionalCharges), 0) as additionalRevenue " +
            "FROM Billing b WHERE b.billDate BETWEEN :startDate AND :endDate AND b.paymentStatus = 'PAID'")
    Object[] getRevenueBreakdownByDateRange(@Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // Get daily billing statistics
    @Query("SELECT b.billDate, COUNT(b), SUM(b.netAmount), SUM(b.totalTaxAmount) FROM Billing b " +
            "WHERE b.billDate BETWEEN :startDate AND :endDate AND b.billStatus = 'FINALIZED' " +
            "GROUP BY b.billDate ORDER BY b.billDate")
    List<Object[]> getDailyBillingStatistics(@Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // Get top customers by bill amount
    @Query("SELECT b.guestName, COUNT(b), SUM(b.netAmount) FROM Billing b " +
            "WHERE b.billDate BETWEEN :startDate AND :endDate AND b.paymentStatus = 'PAID' " +
            "GROUP BY b.guestName ORDER BY SUM(b.netAmount) DESC")
    List<Object[]> getTopCustomersByRevenue(@Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
