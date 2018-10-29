package com.api.controller;

import com.api.controller.dto.BaseResult;
import com.api.controller.dto.ResultCode;
import com.api.model.Product;
import com.api.model.ProductType;
import com.api.service.ProductTypeService;
import com.api.service.UserService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        List<ProductType> types = productTypeService.checkIsExists(productType);
        if(types!=null&&types.size()>0){
            return new BaseResult(ResultCode.SUCCESS,"该产品类型已存在",null);
        }
        productTypeService.addProductType(productType);
        return result;
    }

    @RequestMapping("removeProductType")
    public BaseResult removeProductType(@RequestParam(name = "productTypeId",value = "productTypeId")Long productTypeId) {
        ProductType productType = productTypeService.getProductType(productTypeId);
        if(productType == null){
            return new BaseResult(ResultCode.SUCCESS,"当前产品类型不存在",null);
        }else{
           productTypeService.deleteProductType(productType);
            return new BaseResult(ResultCode.SUCCESS,"删除成功",null);
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
        pro.setName(productType.getName());
        List<ProductType> types = productTypeService.checkIsExists(pro);
        if(types!=null&&types.size()>0){
            return new BaseResult(ResultCode.SUCCESS,"该产品类型已存在",null);
        }
        productTypeService.modifyProductType(pro);
        return   new BaseResult(ResultCode.SUCCESS,"修改成功",null);
    }

    @RequestMapping("inquireProductTypes")
    public BaseResult inquireProductTypes(
            @RequestParam(name = "name",value = "name" ,required = false)String name,
            @RequestParam(name = "pageSize",value = "pageSize")Integer pageSize,
            @RequestParam(name = "pageIndex",value = "pageIndex")Integer pageIndex) {
        BaseResult result = new BaseResult();
        ProductType productType = new ProductType();
        if(StringUtils.isNotBlank(name)){
            productType.setName(name);
        }
        PageInfo<ProductType> datas =  productTypeService.inquireProductTypes(productType,pageIndex,pageSize);
        Map<String,Object> resultMap = new HashMap<String, Object>(10);
        resultMap.put("productTypes",datas);
        result.setData(resultMap);
        return result;
    }


    @RequestMapping("fetchProductType")
    public BaseResult fetchProductType() {
        BaseResult result = new BaseResult();
        List<ProductType> datas =  productTypeService.inquireProductTypeList(new ProductType());
        Map<String,Object> resultMap = new HashMap<String, Object>(10);
        resultMap.put("productTypes",datas);
        result.setData(resultMap);
        return result;
    }

    @ResponseBody
    @RequestMapping("fetchProductTypeDetail")
    public BaseResult fetchProductTypeDetail(@RequestParam(name = "productTypeId", value = "productTypeId") Long productTypeId) {
        BaseResult result = new BaseResult();
        ProductType productType = productTypeService.getProductType(productTypeId);
        if (productType == null) {
            result.setMessage("当前产品类型不存在，请联系管理员");
        } else {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("productType", productType);
            result.setData(resultMap);
        }
        return result;
    }
}
