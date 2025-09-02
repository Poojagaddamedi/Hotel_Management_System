package com.hotel.erp.repository;

import com.hotel.erp.entity.Checkout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CheckoutRepository extends JpaRepository<Checkout, Long> {

    // Find by folio number
    Optional<Checkout> findByFolioNo(String folioNo);

    // Find by reservation number
    Optional<Checkout> findByReservationNo(String reservationNo);

    // Find by guest name
    List<Checkout> findByGuestNameContainingIgnoreCase(String guestName);

    // Find by room number
    List<Checkout> findByRoomNo(String roomNo);

    // Find by bill number
    Optional<Checkout> findByBillNumber(String billNumber);

    // Find by checkout date range
    List<Checkout> findByCheckOutDateBetween(LocalDate startDate, LocalDate endDate);

    // Find by checkout status
    List<Checkout> findByCheckoutStatus(String checkoutStatus);

    // Find by payment status
    List<Checkout> findByPaymentStatus(String paymentStatus);

    // Find by checkout processed by
    List<Checkout> findByCheckoutProcessedBy(Long checkoutProcessedBy);

    // Find pending checkouts
    @Query("SELECT c FROM Checkout c WHERE c.checkoutStatus = 'IN_PROGRESS' OR c.paymentStatus = 'PENDING'")
    List<Checkout> findPendingCheckouts();

    // Find completed checkouts
    @Query("SELECT c FROM Checkout c WHERE c.checkoutStatus = 'COMPLETED' AND c.paymentStatus = 'PAID'")
    List<Checkout> findCompletedCheckouts();

    // Find checkouts with outstanding balance
    @Query("SELECT c FROM Checkout c WHERE c.balanceAmount > 0")
    List<Checkout> findCheckoutsWithOutstandingBalance();

    // Get total revenue for date range
    @Query("SELECT COALESCE(SUM(c.totalBillAmount), 0) FROM Checkout c WHERE c.checkOutDate BETWEEN :startDate AND :endDate AND c.checkoutStatus = 'COMPLETED'")
    BigDecimal getTotalRevenueByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // Get total outstanding amount
    @Query("SELECT COALESCE(SUM(c.balanceAmount), 0) FROM Checkout c WHERE c.balanceAmount > 0")
    BigDecimal getTotalOutstandingAmount();

    // Get checkout count by status
    @Query("SELECT COUNT(c) FROM Checkout c WHERE c.checkoutStatus = :status")
    Long countByCheckoutStatus(@Param("status") String status);

    // Get average feedback rating
    @Query("SELECT AVG(c.feedbackRating) FROM Checkout c WHERE c.feedbackRating IS NOT NULL")
    Double getAverageFeedbackRating();

    // Find checkouts by company
    List<Checkout> findByCompanyNameContainingIgnoreCase(String companyName);

    // Advanced search
    @Query("SELECT c FROM Checkout c WHERE " +
            "(:folioNo IS NULL OR c.folioNo = :folioNo) AND " +
            "(:guestName IS NULL OR LOWER(c.guestName) LIKE LOWER(CONCAT('%', :guestName, '%'))) AND " +
            "(:roomNo IS NULL OR c.roomNo = :roomNo) AND " +
            "(:startDate IS NULL OR c.checkOutDate >= :startDate) AND " +
            "(:endDate IS NULL OR c.checkOutDate <= :endDate) AND " +
            "(:checkoutStatus IS NULL OR c.checkoutStatus = :checkoutStatus) AND " +
            "(:paymentStatus IS NULL OR c.paymentStatus = :paymentStatus)")
    List<Checkout> findByMultipleFilters(
            @Param("folioNo") String folioNo,
            @Param("guestName") String guestName,
            @Param("roomNo") String roomNo,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("checkoutStatus") String checkoutStatus,
            @Param("paymentStatus") String paymentStatus);

    // Get daily checkout statistics
    @Query("SELECT c.checkOutDate, COUNT(c), SUM(c.totalBillAmount) FROM Checkout c " +
            "WHERE c.checkOutDate BETWEEN :startDate AND :endDate AND c.checkoutStatus = 'COMPLETED' " +
            "GROUP BY c.checkOutDate ORDER BY c.checkOutDate")
    List<Object[]> getDailyCheckoutStatistics(@Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
