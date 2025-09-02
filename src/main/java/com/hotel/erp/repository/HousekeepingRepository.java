package com.hotel.erp.repository;

import com.hotel.erp.entity.Housekeeping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HousekeepingRepository extends JpaRepository<Housekeeping, Long> {

    /**
     * Find tasks by room number
     * 
     * @param roomNo the room number
     * @return List of tasks for the given room number
     */
    List<Housekeeping> findByRoomNo(String roomNo);

    /**
     * Find tasks by status
     * 
     * @param status the status
     * @return List of tasks for the given status
     */
    List<Housekeeping> findByStatus(String status);

    /**
     * Find tasks by assigned to
     * 
     * @param assignedTo the assigned to
     * @return List of tasks for the given assigned to
     */
    List<Housekeeping> findByAssignedTo(String assignedTo);

    /**
     * Find tasks by task date
     * 
     * @param taskDate the task date
     * @return List of tasks for the given date
     */
    List<Housekeeping> findByTaskDate(LocalDate taskDate);

    /**
     * Find tasks between dates
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return List of tasks between the given dates
     */
    List<Housekeeping> findByTaskDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Custom query to search tasks by any field
     * 
     * @param query the search query
     * @return List of tasks matching the query
     */
    @Query("SELECT h FROM Housekeeping h WHERE " +
           "h.roomNo LIKE %:query% OR " +
           "h.status LIKE %:query% OR " +
           "h.assignedTo LIKE %:query% OR " +
           "h.remarks LIKE %:query%")
    List<Housekeeping> searchTasks(@Param("query") String query);

    /**
     * Find tasks by room number and status
     * 
     * @param roomNo the room number
     * @param status the status
     * @return List of tasks for the given room number and status
     */
    List<Housekeeping> findByRoomNoAndStatus(String roomNo, String status);

    /**
     * Find tasks by assigned to and status
     * 
     * @param assignedTo the assigned to
     * @param status the status
     * @return List of tasks for the given assigned to and status
     */
    List<Housekeeping> findByAssignedToAndStatus(String assignedTo, String status);
} 