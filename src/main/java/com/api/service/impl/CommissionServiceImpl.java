package com.api.service.impl;

import com.api.dao.CommissionDao;
import com.api.dao.CommissionDetailDao;
import com.api.dao.WithdrawDao;
import com.api.model.Commission;
import com.api.model.CommissionDetail;
import com.api.model.Withdraw;
import com.api.model.vo.WithdrawVo;
import com.api.service.CommissionService;
import com.github.pagehelper.PageHelper;
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
    public Commission fetchCommission(Long uid) {

        return commissionDao.selectById(uid);
    }

    @Override
    public List<WithdrawVo> fetchWithout(Integer start, Integer length) {
        PageHelper.startPage(start, length);
        List<WithdrawVo> withdrawVos = withdrawDao.fetchWithdrawList();
        return withdrawVos;
    }

    @Override
    public List<CommissionDetail> fetchCommissionDetail(Long userId,Integer start, Integer length) {
        PageHelper.startPage(start, length);
        return commissionDetailDao.selectByUserId(userId);
    }

    @Override
    public boolean finshWithout(Long id) {
        if (id == null) {
            logger.error("id 不能为空");
            return false;
        }
        Withdraw withdraw = new Withdraw();
        withdraw.setId(id);
        withdraw.setStatus((byte)1);
        int result = withdrawDao.updateSelective(withdraw);
        return result == 1;
    }

    @Override
    public List<WithdrawVo> fetchWithdrawByUid(Long uid) {
        return withdrawDao.fetchWithdrawByUid(uid);
    }

    @Override
    public int applyWithdraw(Withdraw withdraw) {
        return withdrawDao.insertSelective(withdraw);
    }
}
