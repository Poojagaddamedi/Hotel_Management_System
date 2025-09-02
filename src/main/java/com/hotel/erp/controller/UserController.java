package com.hotel.erp.controller;

import com.hotel.erp.dto.UserDTO;
import com.hotel.erp.service.UserService;
import com.hotel.erp.enums.UserRole;
import com.hotel.erp.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Marks this as a RESTful controller
@RequestMapping("/api/users") // Base URL for user APIs
public class UserController {
    private final UserService userService; // Service dependency

    public UserController(UserService userService) { // Simplified constructor
        this.userService = userService;
    }

    @PostMapping // Handles POST requests to create a user
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        try {
            return ResponseEntity.ok(userService.createUser(userDTO)); // Returns created user
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping // Handles GET requests to list all users
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        try {
            return ResponseEntity.ok(userService.getAllUsers()); // Returns all users
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{userId}") // Handles GET requests by ID
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer userId) {
        try {
            return ResponseEntity.ok(userService.getUserById(userId)); // Returns user by ID
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build(); // Returns 404 for user not found
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{userId}") // Handles PUT requests to update a user
    public ResponseEntity<UserDTO> updateUser(@PathVariable Integer userId, @RequestBody UserDTO userDTO) {
        try {
            return ResponseEntity.ok(userService.updateUser(userId, userDTO)); // Returns updated user
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build(); // Returns 404 for user not found
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{userId}") // Handles DELETE requests to remove a user
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
        try {
            userService.deleteUser(userId); // Deletes user
            return ResponseEntity.noContent().build(); // Returns 204 No Content
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/username/{username}") // Handles GET requests by username
    public ResponseEntity<UserDTO> findByUsername(@PathVariable String username) {
        try {
            UserDTO user = userService.findByUsername(username); // Finds user by username
            return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build(); // Returns user or 404
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Role-based endpoints
    @PostMapping("/admin/create")
    public ResponseEntity<UserDTO> createUserByAdmin(@RequestBody UserDTO userDTO, Authentication authentication) {
        try {
            if (authentication != null) {
                return ResponseEntity.ok(userService.createUserByAdmin(userDTO, authentication.getName()));
            } else {
                return ResponseEntity.ok(userService.createUser(userDTO)); // Fallback to regular user creation
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/created-by/{username}")
    public ResponseEntity<List<UserDTO>> getUsersCreatedBy(@PathVariable String username) {
        try {
            return ResponseEntity.ok(userService.getUsersCreatedBy(username));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/created-by-me")
    public ResponseEntity<List<UserDTO>> getUsersCreatedByMe(Authentication authentication) {
        try {
            return ResponseEntity.ok(userService.getUsersCreatedBy(authentication.getName()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{userId}/toggle-status")
    public ResponseEntity<UserDTO> toggleUserStatus(@PathVariable Integer userId, Authentication authentication) {
        try {
            String username = authentication != null ? authentication.getName() : "SYSTEM";
            return ResponseEntity.ok(userService.toggleUserStatus(userId, username));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserDTO>> getUsersByRole(@PathVariable UserRole role) {
        try {
            return ResponseEntity.ok(userService.getUsersByRole(role));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
        try {
            UserDTO user = userService.findByUsername(authentication.getName());
            return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // REMOVED: UserRights related endpoints that were causing dependency issues
    // These can be re-added once UserRights functionality is properly fixed
}