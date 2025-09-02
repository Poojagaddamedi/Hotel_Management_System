package com.hotel.erp.repository;

import com.hotel.erp.entity.Checkin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CheckinRepository extends JpaRepository<Checkin, Long> {

    /**
     * Find a checkin by its folio number
     * 
     * @param folioNo the folio number
     * @return Optional containing the Checkin if found
     */
    Optional<Checkin> findByFolioNo(String folioNo);

    /**
     * Find first checkin by folio number (for handling duplicates)
     * 
     * @param folioNo the folio number
     * @return Optional containing the first Checkin if found
     */
    Optional<Checkin> findFirstByFolioNo(String folioNo);

    /**
     * Find all active checkins (where status is 'CHECKED_IN')
     * 
     * @return List of active checkins
     */
    @Query("SELECT c FROM Checkin c WHERE c.status = 'CHECKED_IN'")
    List<Checkin> findAllActiveCheckins();

    /**
     * Find all checkins for a specific room number
     * 
     * @param roomNo the room number
     * @return List of checkins for the given room number
     */
    @Query("SELECT c FROM Checkin c WHERE c.roomNo = :roomNo")
    List<Checkin> findByRoomNo(@Param("roomNo") String roomNo);

    /**
     * Find all checkins for a specific customer ID
     * 
     * @param customerId the customer ID
     * @return List of checkins for the given customer ID
     */
    List<Checkin> findByCustomerId(Integer customerId);

    /**
     * Find all checkins between two dates
     * 
     * @param startDate the start date
     * @param endDate   the end date
     * @return List of checkins between the given dates
     */
    @Query("SELECT c FROM Checkin c WHERE c.checkInDate >= :startDate AND c.checkOutDate <= :endDate")
    List<Checkin> findCheckinsBetweenDates(@Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    /**
     * Count the number of active checkins
     * 
     * @return Count of active checkins
     */
    @Query("SELECT COUNT(c) FROM Checkin c WHERE c.status = 'CHECKED_IN'")
    Long countActiveCheckins();

    /**
     * Find all checkins that are due to check out today
     * 
     * @param today today's date
     * @return List of checkins due to check out today
     */
    List<Checkin> findByCheckOutDateAndStatus(LocalDate today, String status);

    /**
     * Find a checkin by reservation number
     * 
     * @param reservationNo the reservation number
     * @return Optional containing the Checkin if found
     */
    Optional<Checkin> findByReservationNo(String reservationNo);

    /**
     * Find all checkins created by a specific user
     * 
     * @param userId the user ID
     * @return List of checkins created by the given user
     */
    List<Checkin> findByUserId(Integer userId);

    /**
     * Find checkins with overdue checkout (checkout date is before today but still
     * have status CHECKED_IN)
     * 
     * @param currentDate the current date
     * @return List of overdue checkins
     */
    @Query("SELECT c FROM Checkin c WHERE c.checkOutDate < :currentDate AND c.status = 'CHECKED_IN'")
    List<Checkin> findOverdueCheckins(@Param("currentDate") LocalDate currentDate);

    /**
     * Find all checkins that have been checked out (checkOutDate is not null)
     * 
     * @return List of checked out checkins
     */
    @Query("SELECT c FROM Checkin c WHERE c.checkOutDate IS NOT NULL")
    List<Checkin> findByCheckOutDateIsNotNull();
}
