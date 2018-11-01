package com.api.dao;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface CommissionDao {
    int addCommissionByUid(Map<String, Object> params);

}