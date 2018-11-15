package com.api.model.vo;

import com.api.model.Product;

import java.io.Serializable;
import java.util.Date;

public class ProductVo extends Product implements Serializable  {
   private Boolean canBuy;

    public Boolean getCanBuy() {
        return canBuy;
    }

    public void setCanBuy(Boolean canBuy) {
        this.canBuy = canBuy;
    }
}