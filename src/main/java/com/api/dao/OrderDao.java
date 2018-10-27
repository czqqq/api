package com.api.dao;

import com.api.model.Order;
import com.api.model.OrderDetail;
import com.api.model.ProductType;
import com.api.model.vo.OrderVo;
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
    public Long insertSelective(Order order);
    List<Order> selectByEntity(Order order);
    public Integer countByEntity(Order order);
    List<OrderVo>  selectJoinByEntity(Order order);
}