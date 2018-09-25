package com.api.dao;

import com.api.model.UserAddress;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAddressDao {
    public UserAddress selectById(Long id);

    public void deleteById(Long id);

    public List<UserAddress> selectByIds(List<Long> ids);

    public void update(UserAddress userAddress);

    public void updateSelective(UserAddress userAddress);

    public Long insert(UserAddress userAddress);

    List<UserAddress> selectByEntity(UserAddress userAddress);

    public Integer countByEntity(UserAddress userAddress);

    List<UserAddress> checkIsExists(UserAddress address);
}