package com.api.service;

import com.api.model.Commission;
import com.api.model.CommissionDetail;
import com.api.model.Withdraw;
import com.api.model.vo.WithdrawVo;

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

    int applyWithdraw(Withdraw withdraw);

}
