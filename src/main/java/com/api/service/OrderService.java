package com.api.service;

import com.api.model.Order;
import com.api.model.vo.OrderDetailVo;
import com.api.model.vo.OrderVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

public interface OrderService {

    /**
     * 修改订单
     * @param order
     * @param details
     * @return
     */
    public Long modifyOrder(OrderVo order, OrderDetailVo details);

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
    public OrderVo  getOrder(Long orderId);

    /**
     * 分页查询
     * @param order
     * @param nextPage
     * @param pageSize
     * @return
     */
    public Page<OrderVo> inquireOrders(OrderVo order,int nextPage,int pageSize );



}
