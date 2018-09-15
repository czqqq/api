package com.api.model.vo;

import com.api.model.OrderDetail;

import java.util.Date;

public class OrderVo {
    private Long id;

    private Long userId;

    private Double totalPrice;

    private Byte status;

    private String statusDesc;

    private String ct;

    private String code;

    private String tradeNumber;

    private String userName;

    private OrderDetailVo orderDetailVo;

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

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTradeNumber() {
        return tradeNumber;
    }

    public void setTradeNumber(String tradeNumber) {
        this.tradeNumber = tradeNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public OrderDetailVo getOrderDetailVo() {
        return orderDetailVo;
    }

    public void setOrderDetailVo(OrderDetailVo orderDetailVo) {
        this.orderDetailVo = orderDetailVo;
    }
}
