package com.api.dao;

import com.api.model.CommissionDetail;
import org.springframework.stereotype.Repository;

@Repository
public interface CommissionDetailDao {
    int insertSelective(CommissionDetail commissionDetail);
}