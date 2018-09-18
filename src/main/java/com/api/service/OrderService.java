package com.api.service;

import com.api.model.Order;
import com.api.model.vo.OrderDetailVo;
import com.api.model.vo.OrderVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface OrderService {

    /**
     * 修改订单
     * @param order
     * @return
     */
    public Long addOrder(OrderVo order);

    /**
     * 删除
     * @param order
     * @return
     */
    public void deleteOrder(OrderVo order) ;

    /**
     * 获取订单
     * @param orderId
     * @return
     */
    public OrderVo  getOrder(Long orderId,Long userId);

    /**
     * 分页查询
     * @param order
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageInfo<OrderVo> inquireOrders(Order order, Integer pageIndex, Integer pageSize);


}
