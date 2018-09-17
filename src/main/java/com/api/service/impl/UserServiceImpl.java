package com.api.service.impl;

import com.api.dao.UserDao;
import com.api.model.User;
import com.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User getUserByLoginName(String loginName) {
        User u = new User();
        u.setMobile(loginName);
        return userDao.selectByEntity(u);
    }
}
