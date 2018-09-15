package com.api.model;

import java.util.Date;

public class CommissionDetail {
    private Long id;

    private Long userId;

    private Long comeby;

    private Double commission;

    private Date ct;

    private Date mt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getComeby() {
        return comeby;
    }

    public void setComeby(Long comeby) {
        this.comeby = comeby;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public Date getCt() {
        return ct;
    }

    public void setCt(Date ct) {
        this.ct = ct;
    }

    public Date getMt() {
        return mt;
    }

    public void setMt(Date mt) {
        this.mt = mt;
    }
}