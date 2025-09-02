package com.hotel.erp.repository;

import com.hotel.erp.entity.Advances;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdvancesRepository extends JpaRepository<Advances, Long> {

    /**
     * Find advances by folio number
     * 
     * @param folioNo the folio number
     * @return List of advances for the given folio number
     */
    List<Advances> findByFolioNo(String folioNo);

    /**
     * Find advances by reservation number
     * 
     * @param reservationNo the reservation number
     * @return List of advances for the given reservation number
     */
    List<Advances> findByReservationNo(String reservationNo);

    /**
     * Find advances by reference number
     * 
     * @param referenceNo the reference number
     * @return List of advances for the given reference number
     */
    List<Advances> findByReferenceNo(String referenceNo);

    /**
     * Find advances by guest name
     * 
     * @param guestName the guest name
     * @return List of advances for the given guest name
     */
    List<Advances> findByGuestName(String guestName);

    /**
     * Check if advance exists by advances_id
     * 
     * @param advancesId the advances ID
     * @return true if exists, false otherwise
     */
    boolean existsByAdvancesId(Long advancesId);

    /**
     * Find advances by payment mode
     * 
     * @param paymentMode the payment mode
     * @return List of advances for the given payment mode
     */
    List<Advances> findByPaymentMode(String paymentMode);

    /**
     * Find advances by payment date
     * 
     * @param paymentDate the payment date
     * @return List of advances for the given payment date
     */
    List<Advances> findByPaymentDate(LocalDate paymentDate);

    /**
     * Find advances between dates
     * 
     * @param startDate the start date
     * @param endDate   the end date
     * @return List of advances between the given dates
     */
    List<Advances> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Search advances by query (partial match on folio_no, reservation_no,
     * reference_no, guest_name)
     * 
     * @param query the search query
     * @return List of advances matching the query
     */
    @Query("SELECT a FROM Advances a WHERE a.folioNo LIKE %:query% OR a.reservationNo LIKE %:query% OR a.referenceNo LIKE %:query% OR a.guestName LIKE %:query%")
    List<Advances> searchAdvances(@Param("query") String query);

    /**
     * Get total amount by folio number
     * 
     * @param folioNo the folio number
     * @return total amount for the folio number
     */
    @Query("SELECT COALESCE(SUM(a.amount), 0) FROM Advances a WHERE a.folioNo = :folioNo")
    BigDecimal getTotalAmountByFolioNo(@Param("folioNo") String folioNo);

    /**
     * Get total amount by reservation number
     * 
     * @param reservationNo the reservation number
     * @return total amount for the reservation number
     */
    @Query("SELECT COALESCE(SUM(a.amount), 0) FROM Advances a WHERE a.reservationNo = :reservationNo")
    BigDecimal getTotalAmountByReservationNo(@Param("reservationNo") String reservationNo);

    /**
     * Get total amount by payment mode
     * 
     * @param paymentMode the payment mode
     * @return total amount for the payment mode
     */
    @Query("SELECT COALESCE(SUM(a.amount), 0) FROM Advances a WHERE a.paymentMode = :paymentMode")
    BigDecimal getTotalAmountByPaymentMode(@Param("paymentMode") String paymentMode);

    /**
     * Get total amount by date range
     * 
     * @param startDate the start date
     * @param endDate   the end date
     * @return total amount for the date range
     */
    @Query("SELECT COALESCE(SUM(a.amount), 0) FROM Advances a WHERE a.paymentDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalAmountByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Get total amount by date
     * 
     * @param date the date
     * @return total amount for the date
     */
    @Query("SELECT COALESCE(SUM(a.amount), 0) FROM Advances a WHERE a.paymentDate = :date")
    BigDecimal getTotalAmountByDate(@Param("date") LocalDate date);

    /**
     * Count advances by payment date
     * 
     * @param paymentDate the payment date
     * @return count of advances for the date
     */
    int countByPaymentDate(LocalDate paymentDate);
}