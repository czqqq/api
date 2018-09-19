package com.api.service.impl;

import com.api.dao.UserAddressDao;
import com.api.model.UserAddress;
import com.api.service.UserAddressService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class UserAddressServiceImpl implements UserAddressService {

    @Autowired
    private UserAddressDao userAddressDao;

    @Override
    @Transactional
    public void addUserAddress(UserAddress userAddress) {
        userAddress.setCt(new Date());
        userAddressDao.insert(userAddress);
    }

    @Override
    public void modifyUserAddress(UserAddress userAddress) {
        userAddress.setMt(new Date());
        userAddressDao.update(userAddress);
    }


    @Override
    @Transactional
    public void deleteUserAddress(UserAddress userAddress) {
        userAddressDao.deleteById(userAddress.getId());
    }

    @Override
    public UserAddress getUserAddress(Long userAddressId, Long userId) {
        UserAddress condition = new UserAddress();
        condition.setId(userAddressId);
        if (userId != null) {
            condition.setUserId(userId);
        }
        List<UserAddress> userAddresss = userAddressDao.selectByEntity(condition);
        if (userAddresss != null && userAddresss.size() > 0) {
            return userAddresss.get(0);
        } else {
            return null;
        }

    }

    @Override
    public PageInfo<UserAddress> inquireUserAddresss(UserAddress userAddress, Integer pageIndex, Integer pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<UserAddress> userAddressList = userAddressDao.selectByEntity(userAddress);
        PageInfo<UserAddress> pageUserAddress = new PageInfo<UserAddress>(userAddressList);
        return pageUserAddress;
    }

    @Override
    public UserAddress getDefaultAddress(Long userId) {
        if (userId == null) return null;
        UserAddress condition = new UserAddress();
        condition.setUserId(userId);
        condition.setIsdefault(Byte.valueOf("1"));
        List<UserAddress> userAddresss = userAddressDao.selectByEntity(condition);
        if (userAddresss != null && userAddresss.size() > 0) {
            return userAddresss.get(0);
        } else {
            return null;
        }
    }
}
