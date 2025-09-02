package com.hotel.erp.dto;

import lombok.Data;
import com.hotel.erp.enums.UserRole;
import java.time.LocalDateTime;

@Data
public class UserDTO {
    private Integer userId;
    private String username;
    private String password; // For input only, never return encrypted password
    private UserRole userRole;
    private String userType; // Keep for backward compatibility
    private String fullName;
    private java.sql.Date creationDate;

    // Audit fields
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
    private Boolean isActive;
    private LocalDateTime lastLogin;

    // Helper method to get display name for user role
    public String getUserRoleDisplayName() {
        return userRole != null ? userRole.getDisplayName() : userType;
    }
}