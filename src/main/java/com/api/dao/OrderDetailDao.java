package com.api.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailDao {
    public void deleteByOrderId(Long orderId);
}