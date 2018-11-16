package com.api.service.impl;

import com.api.dao.ProductDao;
import com.api.model.Product;
import com.api.model.vo.OrderVo;
import com.api.model.vo.ProductVo;
import com.api.service.ProductService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDao productDao;

    @Override
    @Transactional
    public void addProduct(Product product) {
        product.setCt(new Date());
        productDao.insertSelective(product);
    }

    @Override
    @Transactional
    public void modifyProduct(Product product) {
        product.setMt(new Date());
        productDao.update(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Product product) {
        productDao.deleteById(product.getId());
    }

    @Override
    public Product getProduct(Long productId) {
        return productDao.selectById(productId);
    }

    @Override
    public PageInfo<ProductVo> inquireProducts(Product product, Integer pageIndex, Integer pageSize) {
        PageHelper.startPage(pageIndex,pageSize);
        List<ProductVo> products = productDao.selectByEntity(product);
        PageInfo<ProductVo> pages = new PageInfo<ProductVo>(products);
        return pages;
    }

    @Override
    public List<Product> checkIsExists(Product product) {
        return productDao.checkIsExists(product);
    }
}
