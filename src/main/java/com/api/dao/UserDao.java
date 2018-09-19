package com.api.dao;

import com.api.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {
    int insertSelective(User user);
    User selectById(Long id);
    List<User> selectByEntity(User user);
}