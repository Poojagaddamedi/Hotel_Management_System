package com.hotel.erp.repository;

import com.hotel.erp.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    
    List<Event> findByEventNo(String eventNo);
    List<Event> findByEventName(String eventName);
    List<Event> findByEventType(String eventType);
    List<Event> findByClientName(String clientName);
    List<Event> findByVenue(String venue);
    List<Event> findByStatus(String status);
    List<Event> findByPaymentStatus(String paymentStatus);
    List<Event> findByEventDate(LocalDate eventDate);
    List<Event> findByEventDateBetween(LocalDate startDate, LocalDate endDate);
    List<Event> findByBookingDate(LocalDate bookingDate);
    List<Event> findByCoordinatorName(String coordinatorName);
    
    @Query("SELECT e FROM Event e WHERE e.eventType = :type AND e.status = :status")
    List<Event> findByEventTypeAndStatus(@Param("type") String type, @Param("status") String status);
    
    @Query("SELECT e FROM Event e WHERE e.venue = :venue AND e.eventDate = :date")
    List<Event> findByVenueAndDate(@Param("venue") String venue, @Param("date") LocalDate date);
    
    @Query("SELECT e FROM Event e WHERE e.eventDate >= :startDate AND e.eventDate <= :endDate AND e.venue = :venue")
    List<Event> findByDateRangeAndVenue(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("venue") String venue);
    
    @Query("SELECT COUNT(e) FROM Event e WHERE e.status = :status")
    Long countByStatus(@Param("status") String status);
    
    @Query("SELECT SUM(e.totalAmount) FROM Event e WHERE e.eventDate BETWEEN :startDate AND :endDate AND e.paymentStatus = 'PAID'")
    Double getTotalRevenue(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT e FROM Event e WHERE e.clientName LIKE %:query% OR e.eventName LIKE %:query% OR e.eventNo LIKE %:query%")
    List<Event> searchEvents(@Param("query") String query);
}
