package com.hotel.erp.service; // Declares the package as 'com.hotel.erp.service', which is where this class resides

import com.hotel.erp.dto.UserDTO; // Imports the UserDTO class for data transfer
import com.hotel.erp.entity.User; // Imports the User entity for database mapping
import com.hotel.erp.repository.UserRepository; // Imports the repository for database operations
import com.hotel.erp.enums.UserRole; // Imports the UserRole enum
import com.hotel.erp.exception.InsufficientPermissionsException; // Custom exception for permissions
import com.hotel.erp.exception.UserNotFoundException; // Custom exception for user not found
import org.springframework.security.crypto.password.PasswordEncoder; // For password encryption
import org.springframework.security.core.Authentication; // For getting current user
import org.springframework.security.core.context.SecurityContextHolder; // For security context
import org.springframework.stereotype.Service; // Marks this as a Spring-managed service bean
import org.slf4j.Logger; // For logging
import org.slf4j.LoggerFactory; // For logging
import java.util.List; // Imports List for collections
import java.util.Optional; // For optional values
import java.time.LocalDateTime; // For timestamps

@Service // Annotates this class as a Spring bean, making it available for dependency
         // injection
public class UserServiceImpl implements UserService { // Defines the class, implementing the UserService interface

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    // Constants to avoid string duplication
    private static final String SYSTEM_USER = "system";
    private static final String USER_NOT_FOUND_PREFIX = "User not found with id: ";
    private static final String ADMIN_PERMISSION_ERROR = "Only admin users can use this method";
    private static final String ADMIN_TOGGLE_ERROR = "Only admin users can toggle user status";
    private static final String STAFF_DEFAULT = "Staff";

    private final UserRepository userRepository; // Declares a private final field for the repository
    private final PasswordEncoder passwordEncoder; // For encrypting passwords

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) { // Constructor injection
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) { // Overrides the createUser method to add a new user
        // Get currently authenticated user for audit trail
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String creatorUsername = authentication != null ? authentication.getName() : SYSTEM_USER;

        // No permission checks - anyone can create any type of user

        User user = new User(); // Creates a new User object
        user.setUsername(userDTO.getUsername()); // Sets the username from the DTO
        setUserPassword(user, userDTO.getPassword()); // Set both password fields securely
        user.setUserRole(userDTO.getUserRole()); // Sets the user role (also sets userType)
        user.setUserType(mapRoleToUserType(userDTO.getUserRole())); // Explicitly set userType string

        // Handle fullName with fallback to username if not provided
        String fullName = userDTO.getFullName();
        if (fullName == null || fullName.trim().isEmpty()) {
            fullName = userDTO.getUsername(); // Use username as fallback
        }
        user.setFullName(fullName); // Sets the full name with fallback

        user.setCreationDate(userDTO.getCreationDate()); // Sets the creation date from the DTO
        user.setCreatedBy(creatorUsername); // Set who created this user
        user.setCreatedAt(LocalDateTime.now()); // Set creation timestamp
        user.setIsActive(true); // New users are active by default

        User savedUser = userRepository.save(user); // Saves the user to the database
        return mapToDTO(savedUser); // Converts the saved user to a DTO and returns it
    }

    /**
     * Create user with explicit creator information (no admin check required)
     */
    public UserDTO createUserByAdmin(UserDTO userDTO, String username) {
        // No admin check - allow anyone to create users

        User user = new User();
        user.setUsername(userDTO.getUsername());
        setUserPassword(user, userDTO.getPassword()); // Use helper method
        user.setUserRole(userDTO.getUserRole());
        user.setUserType(mapRoleToUserType(userDTO.getUserRole())); // Explicitly set userType string
        user.setFullName(userDTO.getFullName());
        user.setCreationDate(userDTO.getCreationDate());
        user.setCreatedBy(username);
        user.setCreatedAt(LocalDateTime.now());
        user.setIsActive(true);

        User savedUser = userRepository.save(user);
        return mapToDTO(savedUser);
    }

    @Override
    public List<UserDTO> getAllUsers() { // Overrides the getAllUsers method to list all users
        return userRepository.findAll().stream() // Gets all users as a stream
                .map(this::mapToDTO) // Maps each User to a DTO using the mapToDTO method
                .toList(); // Collects the stream into an unmodifiable List
    }

    @Override
    public UserDTO getUserById(Integer userId) { // Overrides the getUserById method to find a user by ID
        User user = userRepository.findById(userId) // Finds the user by ID
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_PREFIX + userId)); // Throws an exception if
                                                                                               // not found
        return mapToDTO(user); // Converts the user to a DTO and returns it
    }

    @Override
    public UserDTO updateUser(Integer userId, UserDTO userDTO) { // Overrides the updateUser method to modify a user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String updaterUsername = authentication != null ? authentication.getName() : SYSTEM_USER;

        User user = userRepository.findById(userId) // Finds the user by ID
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_PREFIX + userId)); // Throws an exception if
                                                                                               // not found

        user.setUsername(userDTO.getUsername()); // Updates the username
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            setUserPassword(user, userDTO.getPassword()); // Use helper method for password update
        }
        user.setUserRole(userDTO.getUserRole()); // Updates the user role
        user.setFullName(userDTO.getFullName()); // Updates the full name
        user.setCreationDate(userDTO.getCreationDate()); // Updates the creation date
        user.setUpdatedBy(updaterUsername); // Set who updated this user
        user.setUpdatedAt(LocalDateTime.now()); // Set update timestamp

        User updatedUser = userRepository.save(user); // Saves the updated user
        return mapToDTO(updatedUser); // Converts the updated user to a DTO and returns it
    }

    @Override
    public void deleteUser(Integer userId) { // Overrides the deleteUser method to remove a user
        userRepository.deleteById(userId); // Deletes the user by ID
    }

    @Override
    public UserDTO findByUsername(String username) { // Overrides the findByUsername method to find a user by username
        try {
            // Try to get the first user by username to avoid duplicate issues
            Optional<User> userOptional = userRepository.findFirstByUsername(username);
            if (userOptional.isPresent()) {
                return mapToDTO(userOptional.get());
            }

            // Fallback to original method with error handling
            User user = userRepository.findByUsername(username);
            return user != null ? mapToDTO(user) : null;
        } catch (org.springframework.dao.IncorrectResultSizeDataAccessException e) {
            // Handle duplicate usernames - get all users with this username and return the
            // first active one
            List<User> users = userRepository.findAllByUsername(username);
            if (!users.isEmpty()) {
                // Prefer active users, then get the first one
                Optional<User> activeUser = users.stream()
                        .filter(u -> u.getIsActive() != null && u.getIsActive())
                        .findFirst();
                if (activeUser.isPresent()) {
                    return mapToDTO(activeUser.get());
                }
                // If no active user, return the first one
                return mapToDTO(users.get(0));
            }
            return null;
        } catch (Exception e) {
            // Log error and return null
            logger.error("Error finding user by username {}: {}", username, e.getMessage());
            return null;
        }
    }

    /**
     * Get all users created by a specific user
     */
    public List<UserDTO> getUsersCreatedBy(String creatorUsername) {
        return userRepository.findByCreatedBy(creatorUsername).stream()
                .map(this::mapToDTO)
                .toList();
    }

    /**
     * Activate or deactivate a user (no authentication required)
     */
    public UserDTO toggleUserStatus(Integer userId, String username) {
        // No admin check - allow anyone to toggle user status
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_PREFIX + userId));

        user.setIsActive(!user.getIsActive());
        user.setUpdatedBy(username);
        user.setUpdatedAt(LocalDateTime.now());

        User updatedUser = userRepository.save(user);
        return mapToDTO(updatedUser);
    }

    /**
     * Get users by role
     */
    public List<UserDTO> getUsersByRole(UserRole role) {
        return userRepository.findByUserRole(role).stream()
                .map(this::mapToDTO)
                .toList();
    }

    private UserDTO mapToDTO(User user) { // Private method to convert User to UserDTO
        UserDTO dto = new UserDTO(); // Creates a new DTO
        dto.setUserId(user.getUserId()); // Sets the user ID
        dto.setUsername(user.getUsername()); // Sets the username
        // Note: We don't set password in DTO for security reasons
        dto.setUserRole(user.getUserRole()); // Sets the user role
        dto.setUserType(user.getUserType()); // Set the actual database userType string
        dto.setFullName(user.getFullName()); // Sets the full name
        dto.setCreationDate(user.getCreationDate()); // Sets the creation date
        dto.setCreatedBy(user.getCreatedBy()); // Sets who created this user
        dto.setCreatedAt(user.getCreatedAt()); // Sets creation timestamp
        dto.setUpdatedBy(user.getUpdatedBy()); // Sets who last updated this user
        dto.setUpdatedAt(user.getUpdatedAt()); // Sets last update timestamp
        dto.setIsActive(user.getIsActive()); // Sets active status
        dto.setLastLogin(user.getLastLogin()); // Sets last login time
        return dto; // Returns the populated DTO
    }

    /**
     * Helper method to set user password in both sha_password and password fields
     */
    private void setUserPassword(User user, String plainPassword) {
        String encodedPassword = passwordEncoder.encode(plainPassword);

        // Set sha_password field (primary storage)
        user.setShaPassword(encodedPassword.getBytes());

        // Set password field for backward compatibility
        user.setPassword(encodedPassword);
    }

    /**
     * Helper method to map UserRole enum to userType string for database storage
     */
    private String mapRoleToUserType(UserRole userRole) {
        if (userRole == null) {
            return STAFF_DEFAULT; // Default fallback
        }

        return switch (userRole) {
            case ADMIN -> "Admin";
            case MANAGER -> "Manager";
            case RECEPTIONIST -> "Receptionist";
            case HOUSEKEEPING -> "Housekeeping";
            case CHEF -> STAFF_DEFAULT;
            case STAFF -> STAFF_DEFAULT;
            case GUEST -> "Guest";
        };
    }
}