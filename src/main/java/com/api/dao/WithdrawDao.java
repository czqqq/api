package com.api.dao;

import com.api.model.vo.WithdrawVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WithdrawDao {
    List<WithdrawVo> fetchWithdrawList();
}