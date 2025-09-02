package com.hotel.erp.repository;

import com.hotel.erp.entity.Laundry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LaundryRepository extends JpaRepository<Laundry, Long> {

    List<Laundry> findByLaundryNo(String laundryNo);

    List<Laundry> findByGuestName(String guestName);

    List<Laundry> findByRoomNo(String roomNo);

    List<Laundry> findByFolioNo(String folioNo);

    List<Laundry> findByStatus(String status);

    List<Laundry> findByServiceType(String serviceType);

    List<Laundry> findByPaymentStatus(String paymentStatus);

    List<Laundry> findByPickupDate(LocalDate pickupDate);

    List<Laundry> findByDeliveryDate(LocalDate deliveryDate);

    List<Laundry> findByPickupDateBetween(LocalDate startDate, LocalDate endDate);

    List<Laundry> findByDeliveryDateBetween(LocalDate startDate, LocalDate endDate);

    List<Laundry> findByAuditDate(LocalDate auditDate);

    List<Laundry> findByAuditDateBetween(LocalDate startDate, LocalDate endDate);

    List<Laundry> findByUrgent(Boolean urgent);

    List<Laundry> findByAssignedTo(String assignedTo);

    @Query("SELECT l FROM Laundry l WHERE l.status = :status AND l.pickupDate = :date")
    List<Laundry> findByStatusAndPickupDate(@Param("status") String status, @Param("date") LocalDate date);

    @Query("SELECT l FROM Laundry l WHERE l.status = :status AND l.urgent = true")
    List<Laundry> findUrgentByStatus(@Param("status") String status);

    @Query("SELECT SUM(l.netAmount) FROM Laundry l WHERE l.auditDate = :date AND l.paymentStatus = 'PAID'")
    Double getTotalRevenueByDate(@Param("date") LocalDate date);

    @Query("SELECT SUM(l.netAmount) FROM Laundry l WHERE l.auditDate BETWEEN :startDate AND :endDate AND l.paymentStatus = 'PAID'")
    Double getTotalRevenueByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(l) FROM Laundry l WHERE l.status = :status")
    Long countByStatus(@Param("status") String status);

    @Query("SELECT COUNT(l) FROM Laundry l WHERE l.status = :status AND l.pickupDate = :date")
    Long countByStatusAndDate(@Param("status") String status, @Param("date") LocalDate date);

    @Query("SELECT l FROM Laundry l WHERE l.guestName LIKE %:query% OR l.roomNo LIKE %:query% OR l.laundryNo LIKE %:query%")
    List<Laundry> searchLaundry(@Param("query") String query);

    @Query("SELECT l FROM Laundry l WHERE l.deliveryDate <= :date AND l.status NOT IN ('DELIVERED', 'CANCELLED')")
    List<Laundry> findOverdueLaundry(@Param("date") LocalDate date);
}
