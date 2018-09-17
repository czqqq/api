package com.api.controller;

import com.api.controller.dto.BaseResult;
import com.api.model.User;
import com.api.service.TestService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private TestService testService;



    @RequestMapping("test")
    public BaseResult test() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return new BaseResult(200, "验证通过", null);
        } else {
            return new BaseResult(200, "验证不通过", null);
        }

    }



    @RequestMapping("testSql")
    public User testSql() {
        User u = testService.testService();
        return u;
    }



}
