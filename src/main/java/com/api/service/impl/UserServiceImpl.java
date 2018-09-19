package com.api.service.impl;

import com.api.dao.UserDao;
import com.api.model.User;
import com.api.service.UserService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User getUserByLoginName(String loginName) {
        User u = new User();
        u.setMobile(loginName);
        List<User> userList = userDao.selectByEntity(u);
        if (userList.size() > 0) {
            return userList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public int addUser(Long introducerId, String mobile, String name, String pwd) {
        User user = new User();
        user.setPid(introducerId);
        user.setMobile(mobile);
        user.setName(name);
        user.setPwd(pwd);
        user.setCt(new Date());
        return  userDao.insertSelective(user);
    }

    @Override
    public List<User> getUsersByPid(Long pid) {
        User user = new User();
        user.setPid(pid);
        List<User>  userList = userDao.selectByEntity(user);
        return userList;
    }
}
