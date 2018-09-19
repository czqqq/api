package com.api.service.impl;

import com.api.dao.ProductDao;
import com.api.model.Product;
import com.api.model.vo.OrderVo;
import com.api.service.ProductService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDao productDao;

    @Override
    public void addProduct(Product product) {
        product.setCt(new Date());
        productDao.insert(product);
    }

    @Override
    public void modifyProduct(Product product) {
        product.setMt(new Date());
        productDao.update(product);
    }

    @Override
    public void deleteProduct(Product product) {
        productDao.deleteById(product.getId());
    }

    @Override
    public Product getProduct(Long productId) {
        return productDao.selectById(productId);
    }

    @Override
    public PageInfo<Product> inquireProducts(Product product, Integer pageIndex, Integer pageSize) {
        PageHelper.startPage(pageIndex,pageSize);
        List<Product> products = productDao.selectByEntity(product);
        PageInfo<Product> pages = new PageInfo<Product>(products);
        return pages;
    }
}
