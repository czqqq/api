package com.api.dao;

import com.api.model.CommissionDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommissionDetailDao {
    int insertSelective(CommissionDetail commissionDetail);
    List<CommissionDetail> selectByUserId(Long userId);
}