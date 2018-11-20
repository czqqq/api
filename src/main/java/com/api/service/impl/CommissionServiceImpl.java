package com.api.service.impl;

import com.api.dao.CommissionDao;
import com.api.dao.CommissionDetailDao;
import com.api.dao.OrderDao;
import com.api.dao.WithdrawDao;
import com.api.model.Commission;
import com.api.model.CommissionDetail;
import com.api.model.Withdraw;
import com.api.model.vo.WithdrawVo;
import com.api.service.CommissionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    @Autowired
    private OrderDao orderDao;

    @Override
    public Commission fetchCommission(Long uid) {

        return commissionDao.selectById(uid);
    }

    @Override
    public List<WithdrawVo> fetchWithout(Integer start, Integer length) {
        if(start != null && length != null){
            PageHelper.startPage(start, length);
        }
        List<WithdrawVo> withdrawVos = withdrawDao.fetchWithdrawList();
        return withdrawVos;
    }

    @Override
    public PageInfo<WithdrawVo> fetchWithdrawListByUser(Integer start, Integer length,Long userId) {
        if(start != null && length != null){
            PageHelper.startPage(start, length);
        }
        List<WithdrawVo> withdrawVos = withdrawDao.fetchWithdrawListByUser(userId);
        PageInfo<WithdrawVo> pages = new PageInfo<WithdrawVo>(withdrawVos);
        return pages;
    }

    @Override
    public List<CommissionDetail> fetchCommissionDetail(Long userId,Integer start, Integer length) {
        if (start != null && length != null) {
            PageHelper.startPage(start, length);
        }
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
        withdraw.setMt(new Date());
        int result = withdrawDao.updateSelective(withdraw);

        if (result > 0) {
            //修改佣金金额
            withdraw = withdrawDao.selectById(id);
            double withoutPrice = withdraw.getMoney();
            Long userId = withdraw.getUserId();
            Commission commission = commissionDao.selectById(userId);
            double commi = commission.getCommission();
            if (withoutPrice > commi) {
                logger.error("提现操作，提现金额出错，超过现有佣金");
            } else {
                commission.setCommission(commi - withoutPrice);
                result = commissionDao.updateSelective(commission);
            }
        }


        return result == 1;
    }

    @Override
    public List<WithdrawVo> fetchWithdrawByUid(Long uid) {
        return withdrawDao.fetchWithdrawByUid(uid);
    }

    @Override
    public List<WithdrawVo> fetchWithdrawhistory(Long userId) {
        List<WithdrawVo> withdrawVos = withdrawDao.fetchWithdrawListByUser(userId);
        return withdrawVos;
    }

    @Override
    public double fetchProfit(Long userId) {
        Double r = withdrawDao.fetchProfit(userId);
        if (r == null) {
            r = 0.0;
        }
        return r;
    }


    @Override
    public int applyWithdraw(Withdraw withdraw) {
        withdraw.setCt(new Date());
        return withdrawDao.insertSelective(withdraw);
    }

    @Override
    public double fetchTodayOutput() {
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            today =  sdf.parse(sdf.format(today));
        } catch (ParseException e) {
            return 0;
        }
        Double t = withdrawDao.fetchTodayOutput(today);
        return t == null ? 0: t ;
    }

    @Override
    public double fetchAllOutput() {
        Double d = withdrawDao.fetchAllOutput();
        if (d == null) {
            d = 0.0;
        }
        return d;
    }

    @Override
    public double fetchAllInput() {
        Double d = orderDao.fetchAllInput();
        if (d == null) {
            d = 0.0;
        }
        return d;
    }
}
