package com.hotel.erp.repository;

import com.hotel.erp.entity.PaymentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaymentReportRepository extends JpaRepository<PaymentReport, Long> {

    /**
     * Find payment reports by report date
     * 
     * @param reportDate the report date
     * @return List of payment reports for the given date
     */
    List<PaymentReport> findByReportDate(LocalDate reportDate);

    /**
     * Find payment reports by payment mode
     * 
     * @param paymentMode the payment mode
     * @return List of payment reports for the given payment mode
     */
    List<PaymentReport> findByPaymentMode(String paymentMode);

    /**
     * Find payment reports by folio number
     * 
     * @param folioNo the folio number
     * @return List of payment reports for the given folio number
     */
    List<PaymentReport> findByFolioNo(String folioNo);

    /**
     * Find payment reports by reservation number
     * 
     * @param reservationNo the reservation number
     * @return List of payment reports for the given reservation number
     */
    List<PaymentReport> findByReservationNo(String reservationNo);

    /**
     * Find payment reports by guest name
     * 
     * @param guestName the guest name
     * @return List of payment reports for the given guest name
     */
    List<PaymentReport> findByGuestName(String guestName);

    /**
     * Find payment reports between report dates
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return List of payment reports between the given dates
     */
    List<PaymentReport> findByReportDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Custom query to fetch payment summaries by date range and payment mode
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @param paymentMode the payment mode (optional)
     * @return List of payment reports matching the criteria
     */
    @Query("SELECT pr FROM PaymentReport pr WHERE " +
           "pr.reportDate BETWEEN :startDate AND :endDate " +
           "AND (:paymentMode IS NULL OR pr.paymentMode = :paymentMode) " +
           "ORDER BY pr.reportDate DESC")
    List<PaymentReport> findPaymentSummariesByDateRangeAndMode(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("paymentMode") String paymentMode);

    /**
     * Custom query to fetch payment summaries by folio number
     * 
     * @param folioNo the folio number
     * @return List of payment reports for the given folio number
     */
    @Query("SELECT pr FROM PaymentReport pr WHERE pr.folioNo = :folioNo ORDER BY pr.reportDate DESC")
    List<PaymentReport> findPaymentSummariesByFolioNo(@Param("folioNo") String folioNo);

    /**
     * Custom query to fetch payment summaries by reservation number
     * 
     * @param reservationNo the reservation number
     * @return List of payment reports for the given reservation number
     */
    @Query("SELECT pr FROM PaymentReport pr WHERE pr.reservationNo = :reservationNo ORDER BY pr.reportDate DESC")
    List<PaymentReport> findPaymentSummariesByReservationNo(@Param("reservationNo") String reservationNo);

    /**
     * Custom query to fetch payment summaries by guest name
     * 
     * @param guestName the guest name
     * @return List of payment reports for the given guest name
     */
    @Query("SELECT pr FROM PaymentReport pr WHERE pr.guestName LIKE %:guestName% ORDER BY pr.reportDate DESC")
    List<PaymentReport> findPaymentSummariesByGuestName(@Param("guestName") String guestName);

    /**
     * Find payment reports by report date and payment mode
     * 
     * @param reportDate the report date
     * @param paymentMode the payment mode
     * @return List of payment reports for the given date and mode
     */
    @Query("SELECT pr FROM PaymentReport pr WHERE pr.reportDate = :reportDate AND pr.paymentMode = :paymentMode")
    List<PaymentReport> findByReportDateAndPaymentMode(@Param("reportDate") LocalDate reportDate, @Param("paymentMode") String paymentMode);

    /**
     * Get total amount by report date
     * 
     * @param reportDate the report date
     * @return total amount for the report date
     */
    @Query("SELECT COALESCE(SUM(pr.totalAmount), 0) FROM PaymentReport pr WHERE pr.reportDate = :reportDate")
    java.math.BigDecimal getTotalAmountByReportDate(@Param("reportDate") LocalDate reportDate);

    /**
     * Get total amount by payment mode
     * 
     * @param paymentMode the payment mode
     * @return total amount for the payment mode
     */
    @Query("SELECT COALESCE(SUM(pr.totalAmount), 0) FROM PaymentReport pr WHERE pr.paymentMode = :paymentMode")
    java.math.BigDecimal getTotalAmountByPaymentMode(@Param("paymentMode") String paymentMode);

    /**
     * Get total amount between report dates
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return total amount between the given dates
     */
    @Query("SELECT COALESCE(SUM(pr.totalAmount), 0) FROM PaymentReport pr WHERE pr.reportDate BETWEEN :startDate AND :endDate")
    java.math.BigDecimal getTotalAmountBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
} 