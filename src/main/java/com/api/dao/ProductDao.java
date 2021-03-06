package com.api.dao;

import com.api.model.OrderDetail;
import com.api.model.Product;
import com.api.model.Product;
import com.api.model.vo.ProductVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDao {
    public Product selectById(Long id);
    public void deleteById(Long id);
    public List<Product> selectByIds(List<Long> ids);
    public void update(Product product);
    public void updateSelective(Product product);
    public Long insert(Product product);
    public Long insertSelective(Product product);
    List<ProductVo> selectByEntity(Product product);
    public Integer countByEntity(Product product);

    List<Product> checkIsExists(Product product);
}