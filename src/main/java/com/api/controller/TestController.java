package com.api.controller;

import com.api.controller.dto.BaseResult;
import com.api.controller.dto.ResultCode;
import com.api.model.User;
import com.api.service.TestService;
import com.api.service.UserService;
import com.api.util.JwtUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @Autowired
    private UserService userService;


    @RequestMapping("test")
    public BaseResult test() {
        Subject subject = SecurityUtils.getSubject();
        String mobile = JwtUtil.getMobileBySubject(subject);
        System.out.println(mobile);
        if (subject.isAuthenticated()) {
            return new BaseResult(200, "验证通过", null);
        } else {
            return new BaseResult(200, "验证不通过", null);
        }

    }



    @RequestMapping("testSql")
    public User testSql(@RequestParam(name = "totalPrice",value = "totalPrice")Double totalPrice, @RequestBody User user) {
        User u = testService.testService();
        return u;
    }


    @RequestMapping("testBuy")
    public BaseResult testBuy(Double price) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return new BaseResult(ResultCode.UNAUTHORIZED, "验证不通过", null);
        }
        String mobile = JwtUtil.getMobileBySubject(subject);
        User user = userService.getUserByLoginName(mobile);

        //支付完成后计算佣金、用户等级
        userService.addParentCommission(user.getId(), price);
        userService.calcUserLevel(user.getId());



        return new BaseResult();
    }



}
