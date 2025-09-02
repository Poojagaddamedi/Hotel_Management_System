package com.hotel.erp.dto;

import lombok.Data;

@Data
public class AccountHeadDTO {
    private Integer accountHeadId;
    private String accountHeadName;
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
