package com.api.controller;

import com.api.controller.dto.BaseResult;
import com.api.controller.dto.ResultCode;
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

    @PostMapping("/regist")
    public BaseResult regist(String mobile, String code, String name, String pwd) {

        //todo 验证短信校验码
        if (code != code) {
            return new BaseResult(ResultCode.FAILURE, "验证码校验失败", null);
        }

        User user = userService.getUserByLoginName(mobile);
        if (user != null) {
            return new BaseResult(ResultCode.FAILURE, "手机号码已经注册过", null);
        }

        int result = userService.addUser(mobile, name, pwd);
        if(result > 0){
            return new BaseResult(ResultCode.SUCCESS, "注册成功", null);
        }else {
            return new BaseResult(ResultCode.FAILURE, "注册失败", null);
        }
    }


    @GetMapping("/fetchAddressList")
    public BaseResult fetchAddressList(){
        BaseResult result = new BaseResult();

        return result;
    }



    @RequestMapping(path = "/401")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public BaseResult unauthorized() {
        return new BaseResult(ResultCode.UNAUTHORIZED, "未授权，请重新登录", null);
    }


}
