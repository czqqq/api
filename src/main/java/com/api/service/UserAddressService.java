package com.api.service;

import com.api.model.UserAddress;
import com.github.pagehelper.PageInfo;

public interface UserAddressService {

    /**
     * 修改用户地址
     * @param userAddress
     * @return
     */
    public void addUserAddress(UserAddress userAddress);

    /**
     * 修改用户收获地址
     * @param userAddress
     * @return
     */
    public void modifyUserAddress(UserAddress userAddress);

    /**
     * 删除
     * @param userAddress
     * @return
     */
    public void deleteUserAddress(UserAddress userAddress) ;

    /**
     * 获取用户地址
     * @param userAddressId
     * @return
     */
    public UserAddress  getUserAddress(Long userAddressId, Long userId);

    /**
     * 分页查询
     * @param userAddress
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageInfo<UserAddress> inquireUserAddresss(UserAddress userAddress, Integer pageIndex, Integer pageSize);


    /**
     * 获取当前用户默认地址
     * @param userId
     * @return
     */
    public UserAddress  getDefaultAddress( Long userId);

}
