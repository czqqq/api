package com.api.dao;

import com.api.model.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDao {
    public Product selectById(Long id);
}