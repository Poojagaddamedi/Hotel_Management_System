package com.hotel.erp.repository;

import com.hotel.erp.entity.User;
import com.hotel.erp.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    // Use Optional to handle cases where user might not exist or duplicates exist
    Optional<User> findFirstByUsername(String username); // Get first user by username to avoid duplicates

    // Keep original method for compatibility but handle duplicates in service layer
    User findByUsername(String username); // Custom method to find a user by username

    List<User> findByCreatedBy(String createdBy); // Find users created by specific user

    List<User> findByUserRole(UserRole userRole); // Find users by role

    List<User> findByIsActive(Boolean isActive); // Find active/inactive users

    List<User> findByUserRoleAndIsActive(UserRole userRole, Boolean isActive); // Find active users by role

    // Custom query to get all users with specific username (for duplicate checking)
    @Query("SELECT u FROM User u WHERE u.username = :username")
    List<User> findAllByUsername(@Param("username") String username);
}