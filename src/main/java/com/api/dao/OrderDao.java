package com.api.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface OrderDao {

    public void deleteById(Long id);
}