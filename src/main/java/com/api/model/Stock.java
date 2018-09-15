package com.api.model;

import java.util.Date;

public class Stock {
    private Long id;

    private Long productId;

    private Integer number;

    private Byte status;

    private Date ct;

    private Date mt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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