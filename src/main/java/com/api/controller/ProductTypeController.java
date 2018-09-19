package com.api.controller;

import com.api.controller.dto.BaseResult;
import com.api.controller.dto.ResultCode;
import com.api.model.ProductType;
import com.api.service.ProductTypeService;
import com.api.service.UserService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProductTypeController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ProductTypeService productTypeService;

    @Autowired
    private UserService userService;


    @RequestMapping("addProductType")
    public BaseResult addProductType(@RequestBody ProductType productType) {
        BaseResult result = new BaseResult();
        productTypeService.addProductType(productType);
        return result;
    }

    @RequestMapping("removeProductType")
    public BaseResult removeProductType(@RequestParam(name = "productTypeId",value = "productTypeId")Long productTypeId) {
        ProductType productType = productTypeService.getProductType(productTypeId);
        if(productType == null){
            return new BaseResult(ResultCode.SUCCESS,"当前产品类型不存在",null);
        }else{
            BaseResult result = new BaseResult();
            Map<String,Object> resultMap = new HashMap<String, Object>();
            resultMap.put("productType",productType);
            result.setData(resultMap);
            return result;
        }

    }

    @RequestMapping("modifyProductType")
    public BaseResult modifyProductType(@RequestBody ProductType productType) {
        if(productType.getId()==null){
            return new BaseResult(ResultCode.SUCCESS,"当前产品类型不存在",null);
        }
        ProductType pro = productTypeService.getProductType(productType.getId());
        if(pro == null){
            return new BaseResult(ResultCode.SUCCESS,"当前产品类型不存在",null);
        }
        BaseResult result = new BaseResult();
        productTypeService.modifyProductType(productType);
        return result;
    }

    @RequestMapping("inquireProductTypes")
    public BaseResult inquireProductTypes( @RequestBody ProductType productType,@RequestParam(name = "pageSize",value = "pageSize")Integer pageSize,
        @RequestParam(name = "pageIndex",value = "pageIndex")Integer pageIndex) {
        BaseResult result = new BaseResult();
        PageInfo<ProductType> datas =  productTypeService.inquireProductTypes(productType,pageIndex,pageSize);
        Map<String,Object> resultMap = new HashMap<String, Object>(10);
        resultMap.put("productTypes",datas);
        result.setData(resultMap);
        return result;
    }


    @RequestMapping("inquireAllProductTypes")
    public BaseResult inquireProductTypes() {
        BaseResult result = new BaseResult();
        List<ProductType> datas =  productTypeService.inquireProductTypeList(new ProductType());
        Map<String,Object> resultMap = new HashMap<String, Object>(10);
        resultMap.put("productTypes",datas);
        result.setData(resultMap);
        return result;
    }
}