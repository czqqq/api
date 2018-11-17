package com.api.controller;

import com.api.controller.dto.BaseResult;
import com.api.controller.dto.DatatablesReq;
import com.api.controller.dto.DatatablesRes;
import com.api.controller.dto.ResultCode;
import com.api.model.Commission;
import com.api.model.CommissionDetail;
import com.api.model.User;
import com.api.model.Withdraw;
import com.api.model.vo.WithdrawVo;
import com.api.service.CommissionService;
import com.api.service.UserService;
import com.api.util.JwtUtil;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class CommissionController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CommissionService commissionService;
    @Autowired
    private UserService userService;



    @PostMapping("/fetchOutPut")
    public BaseResult fetchOutPut() {
        BaseResult res = new BaseResult();
        double d = commissionService.fetchTodayOutput();
        res.setData(d);
        return res;
    }

    @PostMapping("/fetcinoutPut")
    public BaseResult fetcinoutPut() {
        BaseResult res = new BaseResult();
        double input = commissionService.fetchAllInput();
        double output = commissionService.fetchAllOutput();
        Map<String,Double> data = new HashMap<>(2);
        data.put("inPut", input);
        data.put("outPut", output);
        res.setData(data);
        return res;
    }




    @PostMapping("/fetchWithdrawListWeb")
    public DatatablesRes fetchWithdrawListWeb(DatatablesReq req) {
        List<WithdrawVo> withdrawVoList = commissionService.fetchWithout(req.getStart(),req.getLength());
        List<WithdrawVo> withdrawVoListAll = commissionService.fetchWithout(null,null);
        DatatablesRes res = new DatatablesRes();
        res.setDraw(req.getDraw());
        res.setRecordsTotal(withdrawVoListAll.size());
        res.setRecordsFiltered(withdrawVoListAll.size());
        res.setData(withdrawVoList);
        return res;
    }


    @PostMapping("/fetchWithdrawList")
    public BaseResult fetchWithdrawList(Integer pageSize, Integer pageIndex) {
        //获取userId
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return new BaseResult(ResultCode.UNAUTHORIZED, "验证不通过", null);
        }
        String mobile = JwtUtil.getMobileBySubject(subject);
        User user = userService.getUserByLoginName(mobile);

        PageInfo<WithdrawVo> withdrawVoList = commissionService.fetchWithdrawListByUser(pageIndex,pageSize,user.getId());
        BaseResult res = new BaseResult();
        res.setData(withdrawVoList);
        return res;
    }



    @PostMapping("/fetchCommissionDetail")
    public DatatablesRes fetchCommissionDetail(Integer draw ,Integer start, Integer length ,Long userId) {
        List<CommissionDetail> commissionDetailList = commissionService.fetchCommissionDetail(userId,start,length);
        List<CommissionDetail> commissionDetailListAll = commissionService.fetchCommissionDetail(userId,null,null);
        DatatablesRes res = new DatatablesRes();
        res.setDraw(draw);
        res.setRecordsTotal(commissionDetailListAll.size());
        res.setRecordsFiltered(commissionDetailListAll.size());
        res.setData(commissionDetailList);
        return res;
    }


    @PostMapping("/finishWithdraw")
    public boolean finishWithdraw(Long id) {
        boolean result = commissionService.finshWithout(id);
        if(!result){
            logger.error("修改提现状态失败");
        }
        return result;
    }

    @GetMapping("/fetchMyCommission")
    public BaseResult fetchMyCommission() {
        //获取userId
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return new BaseResult(ResultCode.UNAUTHORIZED, "验证不通过", null);
        }
        String mobile = JwtUtil.getMobileBySubject(subject);
        User user = userService.getUserByLoginName(mobile);

        BaseResult result = new BaseResult();

        Commission commission = commissionService.fetchCommission(user.getId());
        if (commission == null) {
            return new BaseResult(ResultCode.FAILURE, "佣金为空", null);
        }
        double commi = commission.getCommission();
        double withdraw = 0;
        List<WithdrawVo> withdrawVos = commissionService.fetchWithdrawByUid(user.getId());
        for (WithdrawVo w : withdrawVos) {
            withdraw += w.getMoney();
        }

        Map<String, Object> resultMap = new HashMap<>(3);
        resultMap.put("commission", commi);
        resultMap.put("withdraw", withdraw);
        resultMap.put("available", commi - withdraw);

        result.setData(resultMap);
        return result;
    }


    @PostMapping("/applyWithdraw")
    public BaseResult applyWithdraw(Withdraw withdrawReq){
        //获取userId
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return new BaseResult(ResultCode.UNAUTHORIZED, "验证不通过", null);
        }
        if (withdrawReq.getMoney() == null || withdrawReq.getMoney() < 100) {
            return new BaseResult(ResultCode.FAILURE, "提现金额不能小于100", null);
        }
        String mobile = JwtUtil.getMobileBySubject(subject);
        User user = userService.getUserByLoginName(mobile);
        Commission commission = commissionService.fetchCommission(user.getId());
        if (commission == null) {
            return new BaseResult(ResultCode.FAILURE, "佣金为空", null);
        }

        double commi = commission.getCommission();
        double withd = 0;
        List<WithdrawVo> withdrawVos = commissionService.fetchWithdrawByUid(user.getId());
        for (WithdrawVo w : withdrawVos) {
            withd += w.getMoney();
        }

        if (withdrawReq.getMoney() > commi + withd) {
            return new BaseResult(ResultCode.FAILURE, "提现金额超出佣金", null);
        }

        BaseResult result = new BaseResult();
        withdrawReq.setUserId(user.getId());
        int res =  commissionService.applyWithdraw(withdrawReq);
        if (res < 1) {
            result.setCode(ResultCode.FAILURE);
            result.setMessage("提现失败");
        }

        return result;
    }
}
