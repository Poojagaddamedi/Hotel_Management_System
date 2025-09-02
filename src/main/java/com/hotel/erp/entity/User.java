package com.hotel.erp.entity;

import jakarta.persistence.*;
import lombok.Data;
import com.hotel.erp.enums.UserRole;
import com.hotel.erp.enums.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;

@Entity
@Table(name = "hotelsoftusers")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(name = "user_name", nullable = false, unique = true)
    private String username;

    @Lob
    @Column(name = "sha_password", columnDefinition = "BLOB")
    private byte[] shaPassword; // Changed to BLOB datatype for secure password storage

    @Column(name = "password")
    private String password; // Keep for backward compatibility during transition

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false, length = 20) // Increased length for longer enum values
    private UserRole userRole;

    // Map to existing database VARCHAR field that references userType table
    @Column(name = "user_type")
    private String userType; // Maps to userType.user_type_name (Admin, Manager, etc.)

    // Additional fields for enhanced functionality
    @Column(name = "branch_id")
    private Integer branchId = 0;

    @Column(name = "client_id")
    private Integer clientId = 0;

    @Column(name = "division_id")
    private Integer divisionId = 0;

    @Column(name = "auth_token")
    private String authToken;

    @Column(name = "session_token")
    private String sessionToken;

    @Column(name = "full_name") // Allow null fullName to be handled gracefully
    private String fullName;

    @Column(name = "creation_date")
    private java.sql.Date creationDate;

    // Audit fields for tracking who created/modified users
    @Column(name = "created_by")
    private String createdBy; // Username of who created this user

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    private String updatedBy; // Username of who last updated this user

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_active")
    private Boolean isActive = true; // Enable/disable users

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    // REMOVED: One-to-Many relationship with UserRights
    // This was causing lazy loading initialization issues and 500 errors
    // UserRights functionality can be managed separately if needed

    // Helper method to get UserType enum from database string
    public UserType getUserTypeEnum() {
        if (userType == null)
            return null;
        try {
            return UserType.valueOf(userType.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Handle database values like "Admin" -> "ADMIN"
            return switch (userType.toLowerCase()) {
                case "admin" -> UserType.ADMIN;
                case "manager" -> UserType.MANAGER;
                case "receptionist" -> UserType.RECEPTIONIST;
                case "housekeeping" -> UserType.HOUSEKEEPING;
                case "maintenance" -> UserType.MAINTENANCE;
                case "accounting" -> UserType.ACCOUNTING;
                case "guest" -> UserType.GUEST;
                case "staff" -> UserType.STAFF;
                default -> null;
            };
        }
    }

    // Helper method to set database string from UserType enum
    public void setUserTypeFromEnum(UserType userTypeEnum) {
        if (userTypeEnum != null) {
            this.userType = userTypeEnum.getDisplayName(); // "Admin", "Manager", etc.
        }
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Manual getters for fields that are causing compilation errors
    public Integer getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public byte[] getShaPassword() {
        return shaPassword;
    }

    public String getFullName() {
        return fullName;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public String getUserType() {
        return userType;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public java.sql.Date getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    // Manual setters for fields that are causing compilation errors
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setShaPassword(byte[] shaPassword) {
        this.shaPassword = shaPassword;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void setCreationDate(java.sql.Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    // Additional missing setters for proper functionality
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public void setDivisionId(Integer divisionId) {
        this.divisionId = divisionId;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public Integer getDivisionId() {
        return divisionId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }
}