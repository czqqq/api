package com.api.service;

import com.api.model.Commission;
import com.api.model.vo.WithdrawVo;

import java.util.List;

public interface CommissionService {
    /**
     * 获取提现列表
     * status = 1
     * @return
     */
    List<WithdrawVo> fetchWithout();
}
