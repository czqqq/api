package com.api.dao;

import com.api.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {
    int insertSelective(User user);
    User selectById(Long id);
    User selectByEntity(User user);
}