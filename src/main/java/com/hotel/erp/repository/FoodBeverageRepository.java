package com.hotel.erp.repository;

import com.hotel.erp.entity.FoodBeverage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FoodBeverageRepository extends JpaRepository<FoodBeverage, Long> {

    List<FoodBeverage> findByOrderNo(String orderNo);

    List<FoodBeverage> findByGuestName(String guestName);

    List<FoodBeverage> findByRoomNo(String roomNo);

    List<FoodBeverage> findByFolioNo(String folioNo);

    List<FoodBeverage> findByOutlet(String outlet);

    List<FoodBeverage> findByOrderType(String orderType);

    List<FoodBeverage> findByStatus(String status);

    List<FoodBeverage> findByPaymentStatus(String paymentStatus);

    List<FoodBeverage> findByOrderTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    List<FoodBeverage> findByAuditDate(LocalDate auditDate);

    List<FoodBeverage> findByAuditDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT f FROM FoodBeverage f WHERE f.outlet = :outlet AND f.status = :status")
    List<FoodBeverage> findByOutletAndStatus(@Param("outlet") String outlet, @Param("status") String status);

    @Query("SELECT f FROM FoodBeverage f WHERE f.waiterName = :waiterName AND f.auditDate = :date")
    List<FoodBeverage> findByWaiterAndDate(@Param("waiterName") String waiterName, @Param("date") LocalDate date);

    @Query("SELECT SUM(f.netAmount) FROM FoodBeverage f WHERE f.outlet = :outlet AND f.auditDate = :date AND f.paymentStatus = 'PAID'")
    Double getTotalRevenueByOutletAndDate(@Param("outlet") String outlet, @Param("date") LocalDate date);

    @Query("SELECT COUNT(f) FROM FoodBeverage f WHERE f.status = :status")
    Long countByStatus(@Param("status") String status);

    @Query("SELECT f FROM FoodBeverage f WHERE f.guestName LIKE %:query% OR f.roomNo LIKE %:query% OR f.orderNo LIKE %:query%")
    List<FoodBeverage> searchOrders(@Param("query") String query);

    @Query("SELECT f FROM FoodBeverage f WHERE f.orderTime BETWEEN :startTime AND :endTime AND f.outlet = :outlet")
    List<FoodBeverage> findByTimeRangeAndOutlet(@Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("outlet") String outlet);
}
