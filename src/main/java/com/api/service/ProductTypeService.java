package com.api.service;

import com.api.model.ProductType;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ProductTypeService {

    /**
     * 添加产品类型
     *
     * @param productType
     * @return
     */
    public void addProductType(ProductType productType);

    /**
     * 修改产品类型
     *
     * @param productType
     */
    void modifyProductType(ProductType productType);

    /**
     * 删除产品类型
     *
     * @param productType
     * @return
     */
    public void deleteProductType(ProductType productType);

    /**
     * 获取产品类型详情
     *
     * @param productTypeId
     * @return
     */
    public ProductType getProductType(Long productTypeId);

    /**
     * 查询列表
     *
     * @param productType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageInfo<ProductType> inquireProductTypes(ProductType productType, Integer pageIndex, Integer pageSize);


    /**
     * 获取产品类型列表
     *
     * @param productType
     * @return
     */
    public List<ProductType> inquireProductTypeList(ProductType productType);


}
