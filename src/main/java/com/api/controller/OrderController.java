package com.api.controller;

import com.api.controller.dto.BaseResult;
import com.api.model.User;
import com.api.service.OrderService;
import com.api.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ResponseBody
    @RequestMapping("test")
    public BaseResult addOrder() {

        return null;
    }
}
