package com.api.model.vo;

import com.api.model.User;

import java.io.Serializable;

public class UserVo extends User implements Serializable {
    private String pName;
    private Double profit;

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }
}
