package com.api.service.impl;

import com.api.dao.UserAddressDao;
import com.api.model.User;
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
        if(userAddress.getIsDefault().equals(Byte.valueOf("0"))){
            userAddressDao.switchAddressToNoDefault(userAddress.getUserId());
        }
        userAddress.setCt(new Date());
        userAddressDao.insertSelective(userAddress);
    }

    @Override
    @Transactional
    public void modifyUserAddress(UserAddress userAddress) {
        if(userAddress.getIsDefault().equals(Byte.valueOf("0"))){
            userAddressDao.switchAddressToNoDefault(userAddress.getUserId());
        }
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
    public List<UserAddress> inquireUserAddresss(UserAddress userAddress) {
        List<UserAddress> userAddressList = userAddressDao.selectByEntity(userAddress);
        return userAddressList;
    }

    @Override
    public UserAddress getDefaultAddress(Long userId) {
        if (userId == null){
            return null;
        }
        UserAddress condition = new UserAddress();
        condition.setUserId(userId);
        condition.setIsDefault(Byte.valueOf("0"));
        List<UserAddress> userAddresss = userAddressDao.selectByEntity(condition);
        if (userAddresss != null && userAddresss.size() > 0) {
            return userAddresss.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<UserAddress> checkIsExists(UserAddress address) {
        return userAddressDao.checkIsExists(address);
    }

    @Override
    @Transactional
    public void setDefaultAddress(UserAddress address, User user) {
        userAddressDao.switchAddressToNoDefault(user.getId());
        address.setMt(new Date());
        userAddressDao.update(address);
    }
}
