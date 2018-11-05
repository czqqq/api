package com.api.dao;

import com.api.model.Token;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenDao {
    int insertSelective(Token token);

    int deleteByUserId(Long userId);

    int deleteById(String token);

    Token selectById(String token);
}