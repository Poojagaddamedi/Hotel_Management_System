package com.hotel.erp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "hotel_account_head")
@Data
public class AccountHead {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "acc_head_id")
    private Integer accountHeadId;

    @Column(name = "acc_head_name", nullable = false)
    private String accountHeadName;

    @Column(name = "acc_head_description")
    private String accountHeadDescription;

    // Manual getters and setters for fields that are causing compilation errors
    public Integer getAccountHeadId() {
        return accountHeadId;
    }

    public void setAccountHeadId(Integer accountHeadId) {
        this.accountHeadId = accountHeadId;
    }

    public String getAccountHeadName() {
        return accountHeadName;
    }

    public void setAccountHeadName(String accountHeadName) {
        this.accountHeadName = accountHeadName;
    }

    public String getAccountHeadDescription() {
        return accountHeadDescription;
    }

    public void setAccountHeadDescription(String accountHeadDescription) {
        this.accountHeadDescription = accountHeadDescription;
    }
}
