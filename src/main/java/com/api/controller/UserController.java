package com.api.controller;

import com.api.controller.dto.*;
import com.api.model.User;
import com.api.model.Withdraw;
import com.api.model.vo.UserVo;
import com.api.model.vo.WithdrawVo;
import com.api.service.CommissionService;
import com.api.service.UserService;
import com.api.util.JwtUtil;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;
    @Autowired
    private CommissionService commissionService;
    @Autowired
    private AndroidVersionDto androidVersion;
    @Autowired
    private IosVersionDto iosVersion;

    @PostMapping("/login")
    public BaseResult login(@RequestParam("mobile") String mobile,
                              @RequestParam("pwd") String pwd) {
        User user = userService.getUserByLoginName(mobile);
        if (user == null) {
            return new BaseResult(ResultCode.NOTUSER, "该用户不存在！", null);
        }else if (user.getPwd().equals(pwd)) {
            Map<String, Object> resultMap =new HashMap<>();
            resultMap.put("name", user.getName());
            String token = JwtUtil.sign(mobile, pwd);
            resultMap.put("token", token);
            userService.signToken(user.getId(), token);
            return new BaseResult(200, "登录成功", resultMap);
        } else {
            return new BaseResult(ResultCode.LOGININVALID, "用户名密码错误！", null);
        }
    }

    @GetMapping("/logout")
    public BaseResult logout() {
        //获取userId
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return new BaseResult(ResultCode.UNAUTHORIZED, "验证不通过", null);
        }
        String mobile = JwtUtil.getMobileBySubject(subject);
        User user = userService.getUserByLoginName(mobile);
        userService.invalidToken(user.getId());
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
        //计算历史盈利
        Double profit = commissionService.fetchProfit(user.getId());

        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        userVo.setPwd(null);
        if (pUser != null) {
            userVo.setpName(pUser.getName());
        }

        userVo.setProfit(profit);

        return new BaseResult(ResultCode.SUCCESS, "成功", userVo);
    }

    @PostMapping("/fetchAllUser")
    public DatatablesRes fetchAllUser(DatatablesReq req) {
        DatatablesRes res = new DatatablesRes();
        List<UserVo> userVos = userService.fetchAllUser(req.getStart(),req.getLength());
        List<User> users = userService.getAll();
        res.setData(userVos);
        res.setDraw(req.getDraw());
        res.setRecordsTotal(users.size());
        res.setRecordsFiltered(users.size());
        return res;
    }

    @PostMapping("/fetchAllUser2")
    public BaseResult fetchAllUser2() {
        BaseResult res = new BaseResult();
        List<User> users = userService.getAll();
        res.setData(users);
        return res;
    }


    @PostMapping("/deleteUserById")
    public BaseResult deleteUserById(Long userId) {
        BaseResult result = new BaseResult();
        int res = userService.deleteUserById(userId);
        if (res > 0) {
            result.setCode(ResultCode.SUCCESS);
            result.setMessage("删除成功");
        }else{
            result.setCode(ResultCode.FAILURE);
            result.setMessage("删除失败");
        }
        return result;
    }

    @PostMapping("/fetchWithdrawHistory")
    public BaseResult fetchWithdrawHistory(String type) {

        //获取userId
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return new BaseResult(ResultCode.UNAUTHORIZED, "验证不通过", null);
        }

        if (type == null) {
            return new BaseResult(ResultCode.FAILURE, "请选择提现类型", null);
        }

        String mobile = JwtUtil.getMobileBySubject(subject);
        User user = userService.getUserByLoginName(mobile);

        BaseResult result = new BaseResult();
        Map<String, String> data = new HashMap<>();
        Withdraw withdraw = userService.fetchWithdrawHistory(type,user.getId());
        if (withdraw != null) {
            data.put("type", withdraw.getType());
            data.put("account", withdraw.getAccount());
            data.put("name", withdraw.getName());
            data.put("mobile", withdraw.getMobile());
            data.put("bank", withdraw.getBank());
            data.put("openingbank", withdraw.getOpeningbank());

            result.setData(data);
        }
        return result;
    }

    @PostMapping(path = "/fetchVersionDownload")
    public BaseResult fetchVersionDownload() {
        BaseResult result = new BaseResult();
        Map<String,Object> value = new HashMap<String,Object>();
        value.put("android",androidVersion);
        value.put("ios",iosVersion);
        result.setData(value);
        return result;
    }

    @RequestMapping(path = "/401")
    @ResponseStatus(HttpStatus.OK)
    public BaseResult unauthorized() {
        return new BaseResult(ResultCode.UNAUTHORIZED, "未授权，请重新登录", null);
    }


}
