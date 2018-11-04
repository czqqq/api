package com.api.dao;

import com.api.model.Commission;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface CommissionDao {
    int addCommissionByUid(Map<String, Object> params);

    Commission selectById(Long userId);
}