package com.api.controller;

import com.api.controller.dto.BaseResult;
import com.api.model.Order;
import com.api.model.User;
import com.api.model.vo.OrderDetailVo;
import com.api.model.vo.OrderVo;
import com.api.service.OrderService;
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
public class OrderController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;


    @RequestMapping("addOrder")
    public BaseResult addOrder(@RequestBody OrderVo order) {
        BaseResult result = new BaseResult();
        //获取userId
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return new BaseResult(200, "验证不通过", null);
        }
        String mobile = JwtUtil.getMobileBySubject(subject);
        User user = userService.getUserByLoginName(mobile);
        order.setStatus(Byte.valueOf("0"));
        order.setUserName(user.getName());
        order.setUserId(user.getId());
        //获取默认地址信息
        List<OrderDetailVo> detailVos = new ArrayList<OrderDetailVo>();
        Long id = orderService.addOrder(order);
        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("id",id);
        result.setData(resultMap);
        return result;
    }

    @ResponseBody
    @RequestMapping("fetchOrderDetail")
    public BaseResult fetchOrderDetail(@RequestParam(name = "orderId",value = "orderId")Long orderId) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return new BaseResult(200, "验证不通过", null);
        }
        BaseResult result = new BaseResult();
        String mobile = JwtUtil.getMobileBySubject(subject);
        User user = userService.getUserByLoginName(mobile);
        OrderVo order = orderService.getOrder(orderId,user.getId());
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
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return new BaseResult(200, "验证不通过", null);
        }
        BaseResult result = new BaseResult();
        //获取地址信息

        String mobile = JwtUtil.getMobileBySubject(subject);
        User user = userService.getUserByLoginName(mobile);
        OrderVo order = orderService.getOrder(orderId,user.getId());
        if(order == null){
            result.setMessage("当前订单不存在，请联系管理员");
        }else{
            //修改订单地址
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("fetchOrder")
    public BaseResult fetchOrder(
            @RequestParam(name = "orderNo",value = "orderNo",required = false)Integer orderNo,
            @RequestParam(name = "pageSize",value = "pageSize")Integer pageSize,
            @RequestParam(name = "pageIndex",value = "pageIndex")Integer pageIndex) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return new BaseResult(200, "验证不通过", null);
        }
        String mobile = JwtUtil.getMobileBySubject(subject);
        User user = userService.getUserByLoginName(mobile);
        Order order = new Order();
        order.setUserId(user.getId());
        PageInfo<OrderVo> datas =  orderService.inquireOrders(order,pageIndex,pageSize);
        BaseResult result = new BaseResult();
        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("orders",datas);
        result.setData(resultMap);
        return result;
    }

    @ResponseBody
    @RequestMapping("fetchOrderWeb")
    public BaseResult fetchOrderWeb(
            @RequestParam(name = "code",value = "code",required = false)String code,
            @RequestParam(name = "mobile",value = "mobile",required = false)String mobile,
            @RequestParam(name = "pageSize",value = "pageSize")Integer pageSize,
            @RequestParam(name = "pageIndex",value = "pageIndex")Integer pageIndex) {
        Order order = new Order();
        if(StringUtil.isNotEmpty(mobile)){
            User user = userService.getUserByLoginName(mobile);
            order.setUserId(user.getId());
        }
        order.setCode(code);
        PageInfo<OrderVo> datas =  orderService.inquireOrders(order,pageIndex,pageSize);
        BaseResult result = new BaseResult();
        Map<String,Object> resultMap = new HashMap<String, Object>(10);
        resultMap.put("orders",datas);
        result.setData(resultMap);
        return result;
    }

}
