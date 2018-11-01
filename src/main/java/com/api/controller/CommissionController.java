package com.api.controller;

import com.api.controller.dto.DatatablesReq;
import com.api.controller.dto.DatatablesRes;
import com.api.model.vo.WithdrawVo;
import com.api.service.CommissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CommissionController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CommissionService commissionService;

    @PostMapping("/fetchWithdrawList")
    public DatatablesRes fetchWithdrawList(DatatablesReq req) {
        List<WithdrawVo> withdrawVoList = commissionService.fetchWithout();
        DatatablesRes res = new DatatablesRes();
        res.setDraw(req.getDraw());
        res.setRecordsTotal(withdrawVoList.size());
        res.setRecordsFiltered(withdrawVoList.size());
        res.setData(withdrawVoList);
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

}
