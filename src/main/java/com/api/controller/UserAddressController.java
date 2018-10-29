package com.api.controller;

import com.api.controller.dto.BaseResult;
import com.api.controller.dto.ResultCode;
import com.api.controller.dto.UserAddressDto;
import com.api.model.UserAddress;
import com.api.model.User;
import com.api.model.UserAddress;
import com.api.service.UserAddressService;
import com.api.service.UserService;
import com.api.util.JwtUtil;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserAddressController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserAddressService userAddressService;

    @Autowired
    private UserService userService;


    @RequestMapping("addUserAddress")
    public BaseResult addUserAddress(@RequestBody UserAddress userAddress) {
        //获取userId
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return new BaseResult(ResultCode.SUCCESS, "验证不通过", null);
        }
        String mobile = JwtUtil.getMobileBySubject(subject);
        User user = userService.getUserByLoginName(mobile);
        BaseResult result = new BaseResult();
        userAddress.setUserId(user.getId());
        List<UserAddress> addresses = userAddressService.checkIsExists(userAddress);
        if(addresses!=null&&addresses.size()>0){
            return new BaseResult(ResultCode.SUCCESS,"该地址已经存在",null);
        }
        userAddressService.addUserAddress(userAddress);
        return result;
    }

    @RequestMapping("removeUserAddress")
    public BaseResult removeUserAddress(@RequestParam(name = "userAddressId",value = "userAddressId")Long userAddressId) {
        //获取userId
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return new BaseResult(ResultCode.SUCCESS, "验证不通过", null);
        }
        String mobile = JwtUtil.getMobileBySubject(subject);
        User user = userService.getUserByLoginName(mobile);
        UserAddress userAddress = userAddressService.getUserAddress(userAddressId,user.getId());
        if(userAddress == null){
            return new BaseResult(ResultCode.SUCCESS,"当前用户地址不存在",null);
        }else{
            BaseResult result = new BaseResult();
            Map<String,Object> resultMap = new HashMap<String, Object>();
            resultMap.put("userAddress",userAddress);
            result.setData(resultMap);
            return result;
        }

    }

    @RequestMapping("setDefaultAddress")
    public BaseResult setDefaultAddress(@RequestParam(name = "userAddressId",value = "userAddressId")Long userAddressId ) {
        //获取userId
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return new BaseResult(ResultCode.SUCCESS, "验证不通过", null);
        }
        String mobile = JwtUtil.getMobileBySubject(subject);
        User user = userService.getUserByLoginName(mobile);
        UserAddress address = userAddressService.getUserAddress(userAddressId,user.getId());
        if(address == null){
            return new BaseResult(ResultCode.SUCCESS,"当前用户地址不存在",null);
        }
        address.setIsDefault(Byte.valueOf("0"));
        userAddressService.setDefaultAddress(address,user);
        return new BaseResult(ResultCode.SUCCESS, "设置成功", null);
    }

    @RequestMapping("modifyUserAddress")
    public BaseResult modifyUserAddress(@RequestBody UserAddress userAddress) {
        if(userAddress.getId()==null){
            return new BaseResult(ResultCode.SUCCESS,"当前用户地址不存在",null);
        }
        //获取userId
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return new BaseResult(ResultCode.SUCCESS, "验证不通过", null);
        }
        String mobile = JwtUtil.getMobileBySubject(subject);
        User user = userService.getUserByLoginName(mobile);
        UserAddress address = userAddressService.getUserAddress(userAddress.getId(),user.getId());
        if(address == null){
            return new BaseResult(ResultCode.SUCCESS,"当前用户地址不存在",null);
        }
        address.setIsDefault(userAddress.getIsDefault());
        address.setRecAddress(userAddress.getRecAddress());
        address.setRecMobile(userAddress.getRecMobile());
        address.setRecName(userAddress.getRecName());
        List<UserAddress> addresses = userAddressService.checkIsExists(address);
        if(addresses!=null&&addresses.size()>0){
            return new BaseResult(ResultCode.SUCCESS,"该地址已经存在",null);
        }
        BaseResult result = new BaseResult();
        userAddressService.modifyUserAddress(address);
        return result;
    }

    @RequestMapping("fetchAddressList")
    public BaseResult fetchAddressList(@RequestBody UserAddressDto userAddress) {
        //获取userId
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return new BaseResult(ResultCode.SUCCESS, "验证不通过", null);
        }
        String mobile = JwtUtil.getMobileBySubject(subject);
        User user = userService.getUserByLoginName(mobile);
        userAddress.setUserId(user.getId());
        BaseResult result = new BaseResult();
        PageInfo<UserAddress> datas =  userAddressService.inquireUserAddresss(userAddress,userAddress.getPageIndex(),userAddress.getPageSize());
        Map<String,Object> resultMap = new HashMap<String, Object>(10);
        resultMap.put("userAddresss",datas);
        result.setData(resultMap);
        return result;
    }

    @ResponseBody
    @RequestMapping("fetchUserAddressDetail")
    public BaseResult fetchUserAddressDetail(@RequestParam(name = "userAddressId", value = "userAddressId") Long userAddressId) {
        BaseResult result = new BaseResult();
        //获取userId
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return new BaseResult(ResultCode.SUCCESS, "验证不通过", null);
        }
        String mobile = JwtUtil.getMobileBySubject(subject);
        User user = userService.getUserByLoginName(mobile);
        UserAddress userAddress = userAddressService.getUserAddress(userAddressId,user.getId());
        if (userAddress == null) {
            result.setMessage("当前地址不存在，请联系管理员");
        } else {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("userAddress", userAddress);
            result.setData(resultMap);
        }
        return result;
    }

}
