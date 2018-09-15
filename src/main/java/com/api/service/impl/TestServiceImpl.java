package com.api.service.impl;

import com.api.dao.UserDao;
import com.api.model.User;
import com.api.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private UserDao userDao;

    @Override
    public User testService() {
        User u = userDao.selectById(1L);
        return u;
    }
}
