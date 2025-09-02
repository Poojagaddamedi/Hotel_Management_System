package com.hotel.erp.repository;

import com.hotel.erp.entity.AdditionalCharges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AdditionalChargesRepository extends JpaRepository<AdditionalCharges, Long> {

    /**
     * Find charges by folio number
     * 
     * @param folioNo the folio number
     * @return List of charges for the given folio number
     */
    List<AdditionalCharges> findByFolioNo(String folioNo);

    /**
     * Find charges by reservation number
     * 
     * @param reservationNo the reservation number
     * @return List of charges for the given reservation number
     */
    List<AdditionalCharges> findByReservationNo(String reservationNo);

    /**
     * Find charges by charge type
     * 
     * @param chargeType the charge type
     * @return List of charges for the given charge type
     */
    List<AdditionalCharges> findByChargeType(String chargeType);

    /**
     * Find charges by charge date
     * 
     * @param chargeDate the charge date
     * @return List of charges for the given date
     */
    List<AdditionalCharges> findByChargeDate(LocalDate chargeDate);

    /**
     * Find charges between dates
     * 
     * @param startDate the start date
     * @param endDate   the end date
     * @return List of charges between the given dates
     */
    List<AdditionalCharges> findByChargeDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Custom query to search charges by any field
     * 
     * @param query the search query
     * @return List of charges matching the query
     */
    @Query("SELECT c FROM AdditionalCharges c WHERE " +
            "c.folioNo LIKE %:query% OR " +
            "c.reservationNo LIKE %:query% OR " +
            "c.chargeType LIKE %:query% OR " +
            "c.remarks LIKE %:query%")
    List<AdditionalCharges> searchCharges(@Param("query") String query);

    /**
     * Get total amount by folio number
     * 
     * @param folioNo the folio number
     * @return total amount for the folio number
     */
    @Query("SELECT COALESCE(SUM(c.amount), 0) FROM AdditionalCharges c WHERE c.folioNo = :folioNo")
    java.math.BigDecimal getTotalAmountByFolioNo(@Param("folioNo") String folioNo);

    /**
     * Get total amount by reservation number
     * 
     * @param reservationNo the reservation number
     * @return total amount for the reservation number
     */
    @Query("SELECT COALESCE(SUM(c.amount), 0) FROM AdditionalCharges c WHERE c.reservationNo = :reservationNo")
    java.math.BigDecimal getTotalAmountByReservationNo(@Param("reservationNo") String reservationNo);

    /**
     * Get total amount by charge type
     * 
     * @param chargeType the charge type
     * @return total amount for the charge type
     */
    @Query("SELECT COALESCE(SUM(c.amount), 0) FROM AdditionalCharges c WHERE c.chargeType = :chargeType")
    java.math.BigDecimal getTotalAmountByChargeType(@Param("chargeType") String chargeType);

    /**
     * Get total amount by date range
     * 
     * @param startDate the start date
     * @param endDate   the end date
     * @return total amount for the date range
     */
    @Query("SELECT COALESCE(SUM(c.amount), 0) FROM AdditionalCharges c WHERE c.chargeDate BETWEEN :startDate AND :endDate")
    java.math.BigDecimal getTotalAmountByDateRange(@Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    /**
     * Get total amount by date
     * 
     * @param date the date
     * @return total amount for the date
     */
    @Query("SELECT COALESCE(SUM(c.amount), 0) FROM AdditionalCharges c WHERE c.chargeDate = :date")
    java.math.BigDecimal getTotalAmountByDate(@Param("date") LocalDate date);
}