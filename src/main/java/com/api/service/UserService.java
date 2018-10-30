package com.api.service;

import com.api.model.User;

import java.util.List;

public interface UserService {

    User getUserByLoginName(String loginName);

    int addUser(Long introducerId, String mobile, String name, String pwd);

    List<User> getUsersByPid(Long pid);

    List<User> getAll();

    /**
     * 用户购买商品后，该用户上级返利
     * @param mobile 用户手机号
     * @param price 产品价格
     * @return
     */
    boolean addParentCommission(String mobile, double price);
}
