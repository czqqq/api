package com.api.service;

import com.api.model.Product;
import com.api.model.vo.ProductVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ProductService {

    /**
     * 添加产品
     * @param product
     * @return
     */
    public void addProduct(Product product);

    void modifyProduct(Product product);

    /**
     * 删除产品
     * @param product
     * @return
     */
    public void deleteProduct(Product product) ;

    /**
     * 获取产品详情
     * @param productId
     * @return
     */
    public Product  getProduct(Long productId);

    /**
     * 查询列表
     * @param product
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageInfo<ProductVo> inquireProducts(Product product, Integer pageIndex, Integer pageSize);

    /**
     * 校验该产品是否存在
     * @param product
     * @return
     */
    List<Product> checkIsExists(Product product);
}
