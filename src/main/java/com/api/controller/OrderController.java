package com.api.controller;

import com.api.controller.dto.BaseResult;
import com.api.model.User;
import com.api.model.vo.OrderDetailVo;
import com.api.model.vo.OrderVo;
import com.api.service.OrderService;
import com.api.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ResponseBody
    @RequestMapping("test")
    public BaseResult addOrder(@RequestParam(name = "totalPrice",value = "totalPrice")Double totalPrice, @RequestBody List<OrderDetailVo> detailVos) {
        OrderVo order = new OrderVo();
        BaseResult result = new BaseResult();
        //获取userId
        //获取默认地址信息

        Long id = orderService.modifyOrder(order,detailVos);
        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("id",id);
        result.setDatas(resultMap);
        return result;
    }
}
