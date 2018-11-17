package com.api.service;

import com.api.model.Commission;
import com.api.model.CommissionDetail;
import com.api.model.Withdraw;
import com.api.model.vo.WithdrawVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CommissionService {

    Commission fetchCommission(Long uid);



    /**
     * 获取提现列表
     * status = 1
     * @return
     */
    List<WithdrawVo> fetchWithout(Integer start, Integer length);
    /**
     * 获取提现列表
     * status = 1
     * @return
     */
    PageInfo<WithdrawVo> fetchWithdrawListByUser(Integer start, Integer length, Long userId);

    /**
     * 获取佣金明细
     * @param userId
     * @return
     */
    List<CommissionDetail>  fetchCommissionDetail(Long userId,Integer start, Integer length);

    /**
     * 完成提现
     * @param id
     * @return
     */
    boolean finshWithout(Long id);

    /**
     * 获取用户申请的提现 - 还未处理完的提现
     * @param uid
     * @return
     */
    List<WithdrawVo> fetchWithdrawByUid(Long uid);

    /**
     * 获取盈利 - 已经完成的提现
     * @param userId
     * @return
     */
    double fetchProfit(Long userId);

    int applyWithdraw(Withdraw withdraw);


    double fetchTodayOutput();

    double fetchAllOutput();
    double fetchAllInput();

}
