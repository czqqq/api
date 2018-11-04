package com.api.model.vo;

import com.api.model.User;

import java.io.Serializable;

public class UserVo extends User implements Serializable {
    String pName;

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }
}
