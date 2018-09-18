package com.api.service;

import com.api.model.User;

public interface UserService {

    User getUserByLoginName(String loginName);

    int addUser(String mobile, String name, String pwd);
}
