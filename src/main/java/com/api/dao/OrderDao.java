package com.api.dao;

import com.api.model.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDao {

    public void deleteById(Long id);
    public Order selectById(Long id);
    public List<Order> selectByIds(List<Long> ids);
    public void update(Order order);
    public void updateSelective(Order order);
    public Long insert(Order order);
}