package com.api.dao;

import com.api.model.Withdraw;
import com.api.model.vo.WithdrawVo;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface WithdrawDao {
    List<WithdrawVo> fetchWithdrawList();
    List<WithdrawVo> fetchWithdrawListByUser(Long userId);

    List<WithdrawVo> fetchWithdrawByUid(Long userId);

    int updateSelective(Withdraw withdraw);

    int insertSelective(Withdraw withdraw);

    Withdraw selectById(Long id);

    Double fetchProfit(Long userId);

    Double fetchTodayOutput(Date mt);

    Double fetchAllOutput();

    Withdraw fetchWithdrawByType(Withdraw withdraw);
}