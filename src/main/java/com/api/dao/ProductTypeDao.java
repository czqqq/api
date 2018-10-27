package com.api.dao;

import com.api.model.Product;
import com.api.model.ProductType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductTypeDao {
    public ProductType selectById(Long id);
    public void deleteById(Long id);
    public List<ProductType> selectByIds(List<Long> ids);
    public void update(ProductType productType);
    public void updateSelective(ProductType productType);
    public Long insert(ProductType productType);
    public Long insertSelective(ProductType productType);
    List<ProductType> selectByEntity(ProductType productType);
    public Integer countByEntity(ProductType productType);
    List<ProductType> checkIsExists(ProductType productType);
}