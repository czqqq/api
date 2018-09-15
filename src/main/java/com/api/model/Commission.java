package com.api.model;

import java.io.Serializable;

public class Commission implements Serializable {
    private Long userId;

    private Double commission;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }
}