package com.hotel.erp.repository;

import com.hotel.erp.entity.UserRights;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for UserRights entity
 */
@Repository
public interface UserRightsRepository extends JpaRepository<UserRights, Integer> {

    /**
     * Find user rights by user ID and function name
     */
    @Query("SELECT ur FROM UserRights ur WHERE ur.user.userId = :userId AND ur.functionName = :functionName")
    Optional<UserRights> findByUserIdAndFunctionName(@Param("userId") Integer userId,
            @Param("functionName") String functionName);

    /**
     * Find all rights for a specific user
     */
    @Query("SELECT ur FROM UserRights ur WHERE ur.user.userId = :userId")
    List<UserRights> findByUserId(@Param("userId") Integer userId);

    /**
     * Find all users who have access to a specific function
     */
    List<UserRights> findByFunctionNameAndHasAccess(String functionName, Boolean hasAccess);

    /**
     * Delete user rights by user ID and function name
     */
    @Query("DELETE FROM UserRights ur WHERE ur.user.userId = :userId AND ur.functionName = :functionName")
    void deleteByUserIdAndFunctionName(@Param("userId") Integer userId, @Param("functionName") String functionName);
}
