package com.hotel.erp.repository;

import com.hotel.erp.entity.GuestService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GuestServiceRepository extends JpaRepository<GuestService, Long> {

    List<GuestService> findByServiceNo(String serviceNo);

    List<GuestService> findByGuestName(String guestName);

    List<GuestService> findByRoomNo(String roomNo);

    List<GuestService> findByFolioNo(String folioNo);

    List<GuestService> findByServiceType(String serviceType);

    List<GuestService> findByStatus(String status);

    List<GuestService> findByPriority(String priority);

    List<GuestService> findByAssignedTo(Long assignedTo);

    List<GuestService> findByDepartment(String department);

    List<GuestService> findByRequestDate(LocalDate requestDate);

    List<GuestService> findByRequestDateBetween(LocalDate startDate, LocalDate endDate);

    List<GuestService> findByChargeable(Boolean chargeable);

    @Query("SELECT g FROM GuestService g WHERE g.serviceType = :type AND g.status = :status")
    List<GuestService> findByServiceTypeAndStatus(@Param("type") String type, @Param("status") String status);

    @Query("SELECT g FROM GuestService g WHERE g.priority = :priority AND g.status = :status")
    List<GuestService> findByPriorityAndStatus(@Param("priority") String priority, @Param("status") String status);

    @Query("SELECT g FROM GuestService g WHERE g.assignedTo = :assignedTo AND g.requestDate = :date")
    List<GuestService> findByAssignedToAndDate(@Param("assignedTo") Long assignedTo, @Param("date") LocalDate date);

    @Query("SELECT g FROM GuestService g WHERE g.preferredTime <= :currentTime AND g.status = 'CONFIRMED'")
    List<GuestService> findDueServices(@Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT g FROM GuestService g WHERE g.status = 'PENDING' AND g.requestDate <= :date")
    List<GuestService> findOverdueRequests(@Param("date") LocalDate date);

    @Query("SELECT COUNT(g) FROM GuestService g WHERE g.status = :status")
    Long countByStatus(@Param("status") String status);

    @Query("SELECT COUNT(g) FROM GuestService g WHERE g.serviceType = :type AND g.requestDate = :date")
    Long countByServiceTypeAndDate(@Param("type") String type, @Param("date") LocalDate date);

    @Query("SELECT SUM(g.actualCost) FROM GuestService g WHERE g.requestDate BETWEEN :startDate AND :endDate AND g.chargeable = true")
    Double getTotalServiceRevenue(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT g FROM GuestService g WHERE g.guestName LIKE %:query% OR g.roomNo LIKE %:query% OR g.serviceNo LIKE %:query% OR g.serviceDescription LIKE %:query%")
    List<GuestService> searchServices(@Param("query") String query);

    @Query("SELECT AVG(g.guestSatisfactionRating) FROM GuestService g WHERE g.guestSatisfactionRating IS NOT NULL AND g.requestDate BETWEEN :startDate AND :endDate")
    Double getAverageRating(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
