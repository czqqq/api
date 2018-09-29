package com.api.dao;

import com.api.model.OrderDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailDao {
    public void deleteByOrderId(Long orderId);
    public void update(OrderDetail detail);
    public void updateSelective(OrderDetail detail);
    public Long insert(OrderDetail detail);
    public Long insertSelective(OrderDetail detail);
    List<OrderDetail> selectByEntity(OrderDetail detail);
}