package com.api.controller;

import com.api.controller.dto.BaseResult;
import com.api.model.User;
import com.api.service.TestService;
import com.api.util.JwtUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

    @Autowired
    private TestService testService;



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



}
