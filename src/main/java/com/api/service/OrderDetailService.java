package com.api.service;

import com.api.model.Order;
import com.api.model.vo.OrderDetailVo;
import com.api.model.vo.OrderVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface OrderDetailService {

    /**
     * 根据订单号获取订单
     * @param orderId
     * @return
     */
    List<OrderDetailVo> getDetails(Long orderId);
}
