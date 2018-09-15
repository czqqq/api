package com.api.model;

import java.util.Date;

public class ProductType {
    private Long id;

    private String name;

    private Date ct;

    private Date mt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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