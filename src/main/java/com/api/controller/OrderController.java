package com.api.controller;

import com.api.controller.dto.BaseResult;
import com.api.model.User;
import com.api.model.vo.OrderDetailVo;
import com.api.model.vo.OrderVo;
import com.api.service.OrderService;
import com.api.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class OrderController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrderService orderService;


    @RequestMapping("addOrder")
    public BaseResult addOrder(@RequestParam(name = "totalPrice",value = "totalPrice")Double totalPrice, @RequestBody List<OrderDetailVo> detailVos) {
        OrderVo order = new OrderVo();
        BaseResult result = new BaseResult();
        //获取userId
        //获取默认地址信息

        Long id = orderService.modifyOrder(order,detailVos);
        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("id",id);
        result.setData(resultMap);
        return result;
    }

    @ResponseBody
    @RequestMapping("fetchOrderDetail")
    public BaseResult fetchOrderDetail(@RequestParam(name = "orderId",value = "orderId")Long orderId) {
        BaseResult result = new BaseResult();
        OrderVo order = orderService.getOrder(orderId);
        if(order == null){
            result.setMessage("当前订单不存在，请联系管理员");
        }else{
            Map<String,Object> resultMap = new HashMap<String, Object>();
            resultMap.put("order",order);
            result.setData(resultMap);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("modifyOrderAddress")
    public BaseResult fetchOrderDetail(@RequestParam(name = "orderId",value = "orderId")Long orderId,@RequestParam(name = "addressId",value = "addressId") Long addressId) {
        BaseResult result = new BaseResult();
        //获取地址信息

        OrderVo order = orderService.getOrder(orderId);
        if(order == null){
            result.setMessage("当前订单不存在，请联系管理员");
        }else{
            //修改订单地址
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("fetchOrder")
    public BaseResult fetchOrder() {
        OrderVo order = new OrderVo();
        BaseResult result = new BaseResult();
        //获取userId
        //获取默认地址信息
        return result;
    }

}
