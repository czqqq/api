package com.api.controller;

import com.api.controller.dto.BaseResult;
import com.api.model.Order;
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




}
