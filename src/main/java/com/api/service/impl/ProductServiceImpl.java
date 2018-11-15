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
        List<Product> products = productDao.selectByEntity(product);
        List<ProductVo> vos = convertPoToVo(products);
        PageInfo<ProductVo> pages = new PageInfo<ProductVo>(vos);
        return pages;
    }

    private List<ProductVo> convertPoToVo(List<Product> products) {
        List<ProductVo> results = new ArrayList<ProductVo>();
        for(Product product : products){
            ProductVo vo = new ProductVo();
            vo.setColor(product.getColor());
            vo.setId(product.getId());
            vo.setName(product.getName());
            vo.setPic(product.getPic());
            vo.setPrice(product.getPrice());
            vo.setRemark(product.getRemark());
            vo.setSize(product.getSize());
            vo.setType(product.getType());
            if(vo.getType() == 4){
                vo.setCanBuy(Boolean.TRUE);
            }else{
                vo.setCanBuy(Boolean.FALSE);
            }
            results.add(vo);
        }
        return results;
    }

    @Override
    public List<Product> checkIsExists(Product product) {
        return productDao.checkIsExists(product);
    }
}
