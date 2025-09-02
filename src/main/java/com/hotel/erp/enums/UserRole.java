package com.hotel.erp.enums;

/**
 * Enum defining different user roles in the hotel management system
 * Each role has specific permissions and capabilities
 */
public enum UserRole {
    ADMIN("Admin", "Full system access, can create any user type"),
    MANAGER("Manager", "Can manage staff and operations, create staff users"),
    STAFF("Staff", "General staff member with basic operational access"),
    RECEPTIONIST("Receptionist", "Handle check-ins, bookings, guest services"),
    HOUSEKEEPING("Housekeeping", "Room cleaning and maintenance tasks"),
    CHEF("Chef", "Kitchen operations and menu management"),
    GUEST("Guest", "Hotel guest with limited access");

    private final String displayName;
    private final String description;

    UserRole(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Check if this role can create users of the specified target role
     */
    public boolean canCreate(UserRole targetRole) {
        return switch (this) {
            case ADMIN -> true; // Admin can create anyone
            case MANAGER -> targetRole != ADMIN; // Manager can create anyone except Admin
            default -> false; // Other roles cannot create users
        };
    }
}
