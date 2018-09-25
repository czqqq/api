package com.api.service.impl;

import com.api.dao.ProductTypeDao;
import com.api.model.ProductType;
import com.api.service.ProductTypeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ProductTypeServiceImpl implements ProductTypeService {

    @Autowired
    ProductTypeDao productTypeDao;

    @Override
    @Transactional
    public void addProductType(ProductType productType) {
        productType.setCt(new Date());
        productTypeDao.insert(productType);
    }

    @Override
    @Transactional
    public void modifyProductType(ProductType productType) {
        productType.setMt(new Date());
        productTypeDao.update(productType);
    }

    @Override
    @Transactional
    public void deleteProductType(ProductType productType) {
        productTypeDao.deleteById(productType.getId());
    }

    @Override
    public ProductType getProductType(Long productTypeId) {
        return productTypeDao.selectById(productTypeId);
    }

    @Override
    public PageInfo<ProductType> inquireProductTypes(ProductType productType, Integer pageIndex, Integer pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<ProductType> productTypes = productTypeDao.selectByEntity(productType);
        PageInfo<ProductType> pages = new PageInfo<ProductType>(productTypes);
        return pages;
    }

    @Override
    public List<ProductType> inquireProductTypeList(ProductType productType) {
        return productTypeDao.selectByEntity(productType);
    }

    @Override
    public List<ProductType> checkIsExists(ProductType productType) {
        return productTypeDao.checkIsExists(productType);
    }
}
