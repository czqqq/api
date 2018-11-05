package com.api.controller;

import com.api.controller.dto.BaseResult;
import com.api.controller.dto.ResultCode;
import com.api.exception.UnauthorizedException;
import com.api.model.User;
import com.api.model.vo.UserVo;
import com.api.service.UserService;
import com.api.util.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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

    @GetMapping("/logout")
    public BaseResult logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return new BaseResult();
    }
    @PostMapping("/regist")
    public BaseResult regist(String mobile, String name, String pwd, String introducerMobile) {

        if (StringUtils.isBlank(introducerMobile)) {
            return new BaseResult(ResultCode.FAILURE, "介绍人不能为空", null);
        }

        User introducer = userService.getUserByLoginName(introducerMobile);
        if (introducer == null) {
            return new BaseResult(ResultCode.FAILURE, "介绍人信息错误", null);
        }
        User user = userService.getUserByLoginName(mobile);
        if (user != null) {
            return new BaseResult(ResultCode.FAILURE, "手机号码已经注册过", null);
        }

        int result = userService.addUser(introducer.getId(),mobile, name, pwd);
        if(result > 0){
            return new BaseResult(ResultCode.SUCCESS, "注册成功", null);

        }else {
            return new BaseResult(ResultCode.FAILURE, "注册失败", null);
        }
    }

    @PostMapping("/fetchUserDetail")
    public BaseResult fetchUserDetail(String mobile) {
        User user = userService.getUserByLoginName(mobile);
        return new BaseResult(ResultCode.SUCCESS, "成功", user);
    }

    @GetMapping("/fetchMyTeam")
    public BaseResult fetchMyTeam() {
        //获取userId
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return new BaseResult(ResultCode.UNAUTHORIZED, "验证不通过", null);
        }
        String mobile = JwtUtil.getMobileBySubject(subject);
        User user = userService.getUserByLoginName(mobile);


        List<User> userList = userService.getUsersByPid(user.getId());
        String[] names = new String[userList.size()];
        for (int i = 0; i < userList.size(); i++) {
            names[i] = StringUtils.isBlank(userList.get(i).getName()) ? userList.get(i).getMobile() :  userList.get(i).getName();
        }

        return new BaseResult(ResultCode.SUCCESS, "成功", names);
    }


    @GetMapping("/fetchUserInfo")
    public BaseResult fetchUserInfo() {
        //获取userId
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return new BaseResult(ResultCode.UNAUTHORIZED, "验证不通过", null);
        }
        String mobile = JwtUtil.getMobileBySubject(subject);
        User user = userService.getUserByLoginName(mobile);
        User pUser = userService.getUserById(user.getPid());

        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        userVo.setPwd(null);
        if (pUser != null) {
            userVo.setpName(pUser.getName());
        }

        return new BaseResult(ResultCode.SUCCESS, "成功", userVo);
    }




    @RequestMapping(path = "/401")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public BaseResult unauthorized() {
        return new BaseResult(ResultCode.UNAUTHORIZED, "未授权，请重新登录", null);
    }


}
