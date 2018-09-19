package com.api.service.impl;

import com.api.dao.ProductDao;
import com.api.model.Product;
import com.api.service.ProductService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDao productDao;

    @Override
    public Long addProduct(Product product) {
        return null;
    }

    @Override
    public Long modifyProduct(Product product) {
        return null;
    }

    @Override
    public void deleteProduct(Product product) {

    }

    @Override
    public Product getProduct(Long productId, Long userId) {
        return null;
    }

    @Override
    public PageInfo<Product> inquireProducts(Product product, Integer pageIndex, Integer pageSize) {
        return null;
    }
}
