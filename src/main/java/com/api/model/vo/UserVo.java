package com.api.model.vo;

import com.api.model.User;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class UserVo extends User implements Serializable {
    private String pName;
    private Double profit;
    private List<Map<String,String>> withDraw;

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

    public List<Map<String, String>> getWithDraw() {
        return withDraw;
    }

    public void setWithDraw(List<Map<String, String>> withDraw) {
        this.withDraw = withDraw;
    }
}
