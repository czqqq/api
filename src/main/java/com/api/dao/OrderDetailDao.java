package com.api.dao;

import com.api.model.Order;
import com.api.model.OrderDetail;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailDao {
    public void deleteByOrderId(Long orderId);
    public void update(OrderDetail detail);
    public void updateSelective(OrderDetail detail);
    public Long insert(OrderDetail detail);
    OrderDetail selectByEntity(OrderDetail detail);
}