package com.api.service.impl;

import com.api.dao.CommissionDao;
import com.api.dao.CommissionDetailDao;
import com.api.dao.WithdrawDao;
import com.api.model.Commission;
import com.api.model.vo.WithdrawVo;
import com.api.service.CommissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommissionServiceImpl implements CommissionService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CommissionDao commissionDao;
    @Autowired
    private CommissionDetailDao commissionDetailDao;
    @Autowired
    private WithdrawDao withdrawDao;

    @Override
    public List<WithdrawVo> fetchWithout() {
        List<WithdrawVo> withdrawVos = withdrawDao.fetchWithdrawList();
        return withdrawVos;
    }
}
