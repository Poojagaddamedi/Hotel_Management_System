package com.hotel.erp.enums;

/**
 * Enum defining different user types in the hotel management system
 */
public enum UserType {
    ADMIN("Administrator", "System administrator with full access"),
    MANAGER("Manager", "Department manager with elevated privileges"),
    RECEPTIONIST("Receptionist", "Front desk operations and guest check-in/out"),
    HOUSEKEEPING("Housekeeping", "Room cleaning and maintenance services"),
    MAINTENANCE("Maintenance", "Technical maintenance and repairs"),
    ACCOUNTING("Accounting", "Financial operations and accounting"),
    GUEST("Guest", "Hotel guest with limited access"),
    STAFF("Staff", "General staff member");

    private final String displayName;
    private final String description;

    UserType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
