package com.api.service;

import com.api.model.User;

import java.util.List;

public interface UserService {

    User getUserByLoginName(String loginName);

    int addUser(Long introducerId, String mobile, String name, String pwd);

    List<User> getUsersByPid(Long pid);

}
