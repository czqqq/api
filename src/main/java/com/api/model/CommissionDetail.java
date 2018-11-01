package com.api.model;

import java.io.Serializable;
import java.util.Date;

public class CommissionDetail implements Serializable {
    private Long id;

    private Long userId;

    private Long comeby;

    private Double commission;

    private String mark;

    private Date ct;

    private Date mt;

    private Byte status;

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

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}