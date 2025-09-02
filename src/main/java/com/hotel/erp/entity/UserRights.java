package com.hotel.erp.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entity representing user access rights for specific functions
 */
@Entity
@Table(name = "user_rights_data")
@Data
public class UserRights {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "function_name", nullable = false)
    private String functionName;

    @Column(name = "has_access", nullable = false)
    private Boolean hasAccess = false;

    // Manual getters and setters for fields that are causing compilation errors
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public Boolean getHasAccess() {
        return hasAccess;
    }

    public void setHasAccess(Boolean hasAccess) {
        this.hasAccess = hasAccess;
    }
}
