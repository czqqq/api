package com.api.service;

import com.api.model.Token;
import com.api.model.User;
import com.api.model.vo.UserVo;

import java.util.List;

public interface UserService {

    User getUserById(Long id);

    User getUserByLoginName(String loginName);

    int addUser(Long introducerId, String mobile, String name, String pwd);

    int deleteUserById(Long userId);

    List<User> getUsersByPid(Long pid);

    List<User> getAll();
    List<UserVo> fetchAllUser(Integer start, Integer length);

    /**
     * 用户购买商品后，该用户上级返利
     * @param userId 用户ID
     * @param price 产品价格
     * @return boolean
     */
    boolean addParentCommission(Long userId, double price);

    /**
     * 用户购买商品后，计算用户等级变更
     * @return int
     */
    int calcUserLevel(Long uid);

    int signToken(Long userId, String token);

    int invalidToken(Long userId);

    int deleteTokenById(String token);

    Token getTokenbyId(String token);

    int saveUserDailyCommission(User user, Double commission);
}
