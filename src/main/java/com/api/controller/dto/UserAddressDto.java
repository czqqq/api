package com.api.controller.dto;

import com.api.model.UserAddress;

/**
 * Created by caizy on 2018/10/29.
 */
public class UserAddressDto extends UserAddress {
    private Integer pageSize;
    private Integer pageIndex;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }
}
