package com.api.controller;

import com.api.controller.dto.BaseResult;
import com.api.controller.dto.ResultCode;
import com.api.model.Order;
import com.api.model.Product;
import com.api.model.User;
import com.api.model.vo.OrderDetailVo;
import com.api.model.vo.OrderVo;
import com.api.service.OrderService;
import com.api.service.ProductService;
import com.api.service.UserService;
import com.api.util.JwtUtil;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProductController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;


    @RequestMapping("addProduct")
    public BaseResult addProduct(@RequestBody Product product) {
        BaseResult result = new BaseResult();
        productService.addProduct(product);
        return result;
    }

    @RequestMapping("removeProduct")
    public BaseResult removeProduct(@RequestParam(name = "productId",value = "productId")Long productId) {
        Product product = productService.getProduct(productId);
        if(product == null){
            return new BaseResult(ResultCode.SUCCESS,"当前产品不存在",null);
        }else{
            BaseResult result = new BaseResult();
            Map<String,Object> resultMap = new HashMap<String, Object>();
            resultMap.put("product",product);
            result.setData(resultMap);
            return result;
        }

    }

    @RequestMapping("modifyProduct")
    public BaseResult modifyProduct(@RequestBody Product product) {
        if(product.getId()==null){
            return new BaseResult(ResultCode.SUCCESS,"当前产品不存在",null);
        }
        Product pro = productService.getProduct(product.getId());
        if(pro == null){
            return new BaseResult(ResultCode.SUCCESS,"当前产品不存在",null);
        }
        BaseResult result = new BaseResult();
        productService.modifyProduct(product);
        return result;
    }

    @RequestMapping("inquireProducts")
    public BaseResult inquireProducts( @RequestBody Product product,@RequestParam(name = "pageSize",value = "pageSize")Integer pageSize,
        @RequestParam(name = "pageIndex",value = "pageIndex")Integer pageIndex) {
        BaseResult result = new BaseResult();
        PageInfo<Product> datas =  productService.inquireProducts(product,pageIndex,pageSize);
        Map<String,Object> resultMap = new HashMap<String, Object>(10);
        resultMap.put("orders",datas);
        result.setData(resultMap);
        return result;
    }

}
