package com.api.controller;

import com.api.controller.dto.BaseResult;
import com.api.exception.UnauthorizedException;
import com.api.model.User;
import com.api.service.UserService;
import com.api.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public BaseResult login(@RequestParam("mobile") String mobile,
                              @RequestParam("pwd") String pwd) {
        User user = userService.getUserByLoginName(mobile);
        Map<String, Object> resultMap =new HashMap<>();
        resultMap.put("name", user.getName());
        resultMap.put("token", JwtUtil.sign(mobile, pwd));
        if (user.getPwd().equals(pwd)) {
            return new BaseResult(200, "登录成功", resultMap);
        } else {
            throw new UnauthorizedException();
        }
    }


    @RequestMapping(path = "/401")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public BaseResult unauthorized() {
        return new BaseResult(401, "Unauthorized", null);
    }


}
