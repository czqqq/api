package com.api.controller;

import com.api.controller.dto.BaseResult;
import com.api.controller.dto.ProductDto;
import com.api.controller.dto.ResultCode;
import com.api.model.Product;
import com.api.model.Product;
import com.api.model.ProductType;
import com.api.model.User;
import com.api.service.ProductService;
import com.api.service.ProductService;
import com.api.service.ProductTypeService;
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
    private ProductTypeService typeService;


    @RequestMapping("addProduct")
    public BaseResult addProduct(@RequestBody Product product) {
        BaseResult result = checkLegal(product);
        if(result!=null){
            return result;
        }
        productService.addProduct(product);
        return new BaseResult(ResultCode.SUCCESS,"添加成功",null);
    }

    @RequestMapping("removeProduct")
    public BaseResult removeProduct(@RequestParam(name = "productId",value = "productId")Long productId) {

        Product product = productService.getProduct(productId);
        if(product == null){
            return new BaseResult(ResultCode.SUCCESS,"当前产品不存在",null);
        }else{
            productService.deleteProduct(product);
            return new BaseResult(ResultCode.SUCCESS,"删除成功",null);
        }

    }
    public BaseResult checkLegal(Product product){
        if(product.getType()==null){
            return new BaseResult(ResultCode.SUCCESS,"请选择产品类型",null);
        }
        ProductType type = typeService.getProductType(product.getType());
        if(type==null){
            return new BaseResult(ResultCode.SUCCESS,"请选择产品类型",null);
        }
        if(product.getName()==null){
            return new BaseResult(ResultCode.SUCCESS,"产品名称不能为空",null);
        }
        if(product.getPrice()==null){
            return new BaseResult(ResultCode.SUCCESS,"产品价格不能为空",null);
        }
        List<Product> types = productService.checkIsExists(product);
        if(types!=null&&types.size()>0){
            return new BaseResult(ResultCode.SUCCESS,"该产品已经存在",null);
        }
        return  null;
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
        BaseResult result = checkLegal(product);
        if(result!=null){
            return result;
        }
        pro.setName(product.getName());
        pro.setPrice(product.getPrice());
        pro.setPic(product.getPic());
        pro.setColor(product.getColor());
        pro.setSize(product.getSize());
        pro.setType(product.getType());
        productService.modifyProduct(pro);
        return new BaseResult(ResultCode.SUCCESS,"修改成功",null);
    }

    @RequestMapping("fetchProduct")
    public BaseResult fetchProduct(@RequestBody ProductDto product) {
        BaseResult result = new BaseResult();
        PageInfo<Product> datas =  productService.inquireProducts(product,product.getPageIndex(),product.getPageSize());
        result.setData(datas);
        return result;
    }

    @ResponseBody
    @RequestMapping("fetchProductDetail")
    public BaseResult fetchProductDetail(@RequestParam(name = "productId", value = "productId") Long productId) {
        BaseResult result = new BaseResult();
        Product product = productService.getProduct(productId);
        if (product == null) {
            result.setMessage("当前产品不存在，请联系管理员");
        } else {
            result.setData(product);
        }
        return result;
    }

}
