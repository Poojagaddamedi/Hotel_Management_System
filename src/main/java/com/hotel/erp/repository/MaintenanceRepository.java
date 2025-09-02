package com.hotel.erp.repository;

import com.hotel.erp.entity.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
        // Find by room number
        List<Maintenance> findByRoomNo(String roomNo);

        // Find overdue requests by date
        @Query("SELECT m FROM Maintenance m WHERE m.status = 'PENDING' AND m.reportedDate <= :date")
        List<Maintenance> findOverdueRequests(@Param("date") java.time.LocalDate date);

        // Find active guest impact requests
        @Query("SELECT m FROM Maintenance m WHERE m.guestImpact = true AND m.status IN ('PENDING','ASSIGNED','IN_PROGRESS')")
        List<Maintenance> findActiveGuestImpactRequests();

        // Search maintenance by query
        @Query("SELECT m FROM Maintenance m WHERE m.roomNo LIKE %:query% OR m.area LIKE %:query% OR m.issueType LIKE %:query% OR m.description LIKE %:query% OR m.vendorName LIKE %:query%")
        List<Maintenance> searchMaintenance(@Param("query") String query);

        // Find by maintenance ID
        Optional<Maintenance> findByMaintenanceId(String maintenanceId);

        // Find by location
        List<Maintenance> findByLocationContainingIgnoreCase(String location);

        // Find by request type
        List<Maintenance> findByRequestType(String requestType);

        // Find by priority
        List<Maintenance> findByPriority(String priority);

        // Find by status
        List<Maintenance> findByStatus(String status);

        // Find by equipment type
        List<Maintenance> findByEquipmentType(String equipmentType);

        // Find by assigned to
        List<Maintenance> findByAssignedTo(Long assignedTo);

        // Find by reported by
        List<Maintenance> findByReportedBy(Long reportedBy);

        // Find by date range
        List<Maintenance> findByReportedDateBetween(LocalDateTime startDate, LocalDateTime endDate);

        // Find pending maintenance requests
        @Query("SELECT m FROM Maintenance m WHERE m.status IN ('PENDING', 'ASSIGNED', 'IN_PROGRESS')")
        List<Maintenance> findPendingMaintenance();

        // Find completed maintenance
        @Query("SELECT m FROM Maintenance m WHERE m.status = 'COMPLETED'")
        List<Maintenance> findCompletedMaintenance();

        // Find overdue maintenance
        @Query("SELECT m FROM Maintenance m WHERE m.estimatedDurationHours IS NOT NULL AND m.startDate IS NOT NULL AND "
                        +
                        "m.status NOT IN ('COMPLETED', 'CANCELLED') AND " +
                        "FUNCTION('TIMESTAMPADD', HOUR, m.estimatedDurationHours, m.startDate) < :currentTime")
        List<Maintenance> findOverdueMaintenance(@Param("currentTime") LocalDateTime currentTime);

        // Find high priority maintenance
        @Query("SELECT m FROM Maintenance m WHERE m.priority IN ('HIGH', 'URGENT') AND m.status != 'COMPLETED'")
        List<Maintenance> findHighPriorityMaintenance();

        // Find emergency maintenance
        @Query("SELECT m FROM Maintenance m WHERE m.requestType = 'EMERGENCY' AND m.status != 'COMPLETED'")
        List<Maintenance> findEmergencyMaintenance();

        // Find maintenance affecting guests
        @Query("SELECT m FROM Maintenance m WHERE m.guestImpact = true AND m.status != 'COMPLETED'")
        List<Maintenance> findMaintenanceAffectingGuests();

        // Find maintenance with blocked rooms
        @Query("SELECT m FROM Maintenance m WHERE m.roomBlocked = true AND m.status != 'COMPLETED'")
        List<Maintenance> findMaintenanceWithBlockedRooms();

        // Find safety concerns
        @Query("SELECT m FROM Maintenance m WHERE m.safetyConcern = true AND m.status != 'COMPLETED'")
        List<Maintenance> findSafetyConcerns();

        // Find recurring issues
        @Query("SELECT m FROM Maintenance m WHERE m.recurringIssue = true")
        List<Maintenance> findRecurringIssues();

        // Find maintenance requiring approval
        @Query("SELECT m FROM Maintenance m WHERE m.approvalRequired = true AND m.approvedBy IS NULL")
        List<Maintenance> findMaintenanceRequiringApproval();

        // Find warranty claims
        @Query("SELECT m FROM Maintenance m WHERE m.warrantyClaim = true")
        List<Maintenance> findWarrantyClaims();

        // Find insurance claims
        @Query("SELECT m FROM Maintenance m WHERE m.insuranceClaim = true")
        List<Maintenance> findInsuranceClaims();

        // Get maintenance count by status
        @Query("SELECT COUNT(m) FROM Maintenance m WHERE m.status = :status")
        Long countByStatus(@Param("status") String status);

        // Get maintenance count by priority
        @Query("SELECT COUNT(m) FROM Maintenance m WHERE m.priority = :priority AND m.status != 'COMPLETED'")
        Long countByPriority(@Param("priority") String priority);

        // Get total maintenance cost for date range
        @Query("SELECT COALESCE(SUM(m.actualCost), 0) FROM Maintenance m WHERE " +
                        "m.completionDate BETWEEN :startDate AND :endDate AND m.status = 'COMPLETED'")
        BigDecimal getTotalMaintenanceCostByDateRange(@Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        // Get average maintenance duration
        @Query("SELECT AVG(m.actualDurationHours) FROM Maintenance m WHERE m.actualDurationHours IS NOT NULL AND m.status = 'COMPLETED'")
        Double getAverageMaintenanceDuration();

        // Find maintenance by multiple filters
        @Query("SELECT m FROM Maintenance m WHERE " +
                        "(:maintenanceId IS NULL OR m.maintenanceId = :maintenanceId) AND " +
                        "(:location IS NULL OR LOWER(m.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
                        "(:requestType IS NULL OR m.requestType = :requestType) AND " +
                        "(:priority IS NULL OR m.priority = :priority) AND " +
                        "(:status IS NULL OR m.status = :status) AND " +
                        "(:equipmentType IS NULL OR m.equipmentType = :equipmentType) AND " +
                        "(:assignedTo IS NULL OR m.assignedTo = :assignedTo) AND " +
                        "(:startDate IS NULL OR m.reportedDate >= :startDate) AND " +
                        "(:endDate IS NULL OR m.reportedDate <= :endDate)")
        List<Maintenance> findByMultipleFilters(
                        @Param("maintenanceId") String maintenanceId,
                        @Param("location") String location,
                        @Param("requestType") String requestType,
                        @Param("priority") String priority,
                        @Param("status") String status,
                        @Param("equipmentType") String equipmentType,
                        @Param("assignedTo") Long assignedTo,
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        // Get maintenance statistics by type
        @Query("SELECT m.requestType, COUNT(m), AVG(m.actualDurationHours), SUM(m.actualCost) FROM Maintenance m " +
                        "WHERE m.completionDate BETWEEN :startDate AND :endDate AND m.status = 'COMPLETED' " +
                        "GROUP BY m.requestType ORDER BY m.requestType")
        List<Object[]> getMaintenanceStatisticsByType(@Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        // Get maintenance statistics by equipment type
        @Query("SELECT m.equipmentType, COUNT(m), AVG(m.actualDurationHours), SUM(m.actualCost) FROM Maintenance m " +
                        "WHERE m.completionDate BETWEEN :startDate AND :endDate AND m.status = 'COMPLETED' " +
                        "GROUP BY m.equipmentType ORDER BY m.equipmentType")
        List<Object[]> getMaintenanceStatisticsByEquipment(@Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        // Get staff performance
        @Query("SELECT m.assignedTo, COUNT(m), AVG(m.actualDurationHours), " +
                        "SUM(CASE WHEN m.status = 'COMPLETED' THEN 1 ELSE 0 END), " +
                        "SUM(CASE WHEN m.qualityCheckStatus = 'PASSED' THEN 1 ELSE 0 END) " +
                        "FROM Maintenance m WHERE m.assignedDate BETWEEN :startDate AND :endDate " +
                        "GROUP BY m.assignedTo ORDER BY m.assignedTo")
        List<Object[]> getStaffPerformance(@Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        // Get daily maintenance statistics
        @Query("SELECT FUNCTION('DATE', m.reportedDate), COUNT(m), " +
                        "SUM(CASE WHEN m.status = 'COMPLETED' THEN 1 ELSE 0 END), " +
                        "SUM(CASE WHEN m.priority IN ('HIGH', 'URGENT') THEN 1 ELSE 0 END) " +
                        "FROM Maintenance m WHERE m.reportedDate BETWEEN :startDate AND :endDate " +
                        "GROUP BY FUNCTION('DATE', m.reportedDate) ORDER BY FUNCTION('DATE', m.reportedDate)")
        List<Object[]> getDailyMaintenanceStatistics(@Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);
}
