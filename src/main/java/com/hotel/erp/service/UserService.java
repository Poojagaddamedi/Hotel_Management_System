package com.hotel.erp.service;

import com.hotel.erp.dto.UserDTO;
import com.hotel.erp.enums.UserRole;
import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO userDTO); // Method to add a new user

    List<UserDTO> getAllUsers(); // Method to list all users

    UserDTO getUserById(Integer userId); // Method to find a user by ID

    UserDTO updateUser(Integer userId, UserDTO userDTO); // Method to update a user

    void deleteUser(Integer userId); // Method to delete a user

    UserDTO findByUsername(String username); // Method to find a user by username

    // New role-based methods
    UserDTO createUserByAdmin(UserDTO userDTO, String adminUsername); // Admin creates user

    List<UserDTO> getUsersCreatedBy(String creatorUsername); // Get users created by someone

    UserDTO toggleUserStatus(Integer userId, String adminUsername); // Activate/deactivate user

    List<UserDTO> getUsersByRole(UserRole role); // Get users by role
}