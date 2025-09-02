package com.hotel.erp.service;

import com.hotel.erp.entity.User;
import com.hotel.erp.entity.UserRights;
import com.hotel.erp.exception.UserNotFoundException;
import com.hotel.erp.repository.UserRepository;
import com.hotel.erp.repository.UserRightsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing user access rights and permissions
 */
@Service
@Transactional
public class UserRightsService {

    private final UserRightsRepository userRightsRepository;
    private final UserRepository userRepository;

    public UserRightsService(UserRightsRepository userRightsRepository, UserRepository userRepository) {
        this.userRightsRepository = userRightsRepository;
        this.userRepository = userRepository;
    }

    /**
     * Assign or update access right for a user to a specific function
     */
    public void assignRight(Integer userId, String functionName, boolean hasAccess) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        // Check if right already exists
        Optional<UserRights> existingRight = userRightsRepository.findByUserIdAndFunctionName(userId, functionName);

        if (existingRight.isPresent()) {
            // Update existing right
            UserRights right = existingRight.get();
            right.setHasAccess(hasAccess);
            userRightsRepository.save(right);
        } else {
            // Create new right
            UserRights right = new UserRights();
            right.setUser(user);
            right.setFunctionName(functionName);
            right.setHasAccess(hasAccess);
            userRightsRepository.save(right);
        }
    }

    /**
     * Check if user has access to a specific function
     */
    public boolean hasAccess(Integer userId, String functionName) {
        return userRightsRepository.findByUserIdAndFunctionName(userId, functionName)
                .map(UserRights::getHasAccess)
                .orElse(false);
    }

    /**
     * Get all rights for a specific user
     */
    public List<UserRights> getUserRights(Integer userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        return userRightsRepository.findByUserId(userId);
    }

    /**
     * Remove access right for a user to a specific function
     */
    public void removeRight(Integer userId, String functionName) {
        userRightsRepository.deleteByUserIdAndFunctionName(userId, functionName);
    }

    /**
     * Get all users who have access to a specific function
     */
    public List<UserRights> getUsersWithAccess(String functionName) {
        return userRightsRepository.findByFunctionNameAndHasAccess(functionName, true);
    }

    /**
     * Bulk assign rights to a user
     */
    public void assignMultipleRights(Integer userId, List<String> functionNames, boolean hasAccess) {
        for (String functionName : functionNames) {
            assignRight(userId, functionName, hasAccess);
        }
    }
}
