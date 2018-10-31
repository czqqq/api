package com.api.service.impl;

import com.api.dao.CommissionDao;
import com.api.dao.CommissionDetailDao;
import com.api.dao.UserDao;
import com.api.model.CommissionDetail;
import com.api.model.User;
import com.api.service.UserService;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserDao userDao;
    @Autowired
    private CommissionDao commissionDao;
    @Autowired
    private CommissionDetailDao commissionDetailDao;

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

    @Override
    public List<User> getAll() {
        User user = new User();
        List<User>  userList = userDao.selectByEntity(user);
        return userList;
    }

    @Override
    public boolean addParentCommission(String mobile, double price) {
        User user = getUserByLoginName(mobile);
        if (user == null) {
            logger.info("用户为空");
            return false;
        }
        Long pid = user.getPid();
        User pUser = userDao.selectById(pid);
        if (pUser == null) {
            logger.error("用户:" + user.getName() + " 上级用户为空");
            return false;
        }

        int level = user.getLevel();
        int pLevel = pUser.getLevel();


        if (pLevel == 0) {
            // 上级用户没有购买过产品，不能获得佣金
            return true;
        }else{

            //佣金分为两部分：一、每次购买产生的佣金；二、如果是第一次购买要加上推荐成功的佣金

            //计算购买产生的佣金
            int buyLevel;
            double buyCommission=0, recommCommission=0,totalCommission=0;
            switch ((int)price) {
                case 800 : buyLevel = 1; break;
                case 6400 : buyLevel = 2; break;
                case 12800 : buyLevel = 3; break;
                case 24800 : buyLevel = 4; break;
                case 56800 : buyLevel = 5; break;
                default: buyLevel=0; break;
            }

            switch (Math.min(pLevel, buyLevel)) {
                case 1: buyCommission = 4; break;
                case 2: buyCommission = 32; break;
                case 3: buyCommission = 64; break;
                case 4: buyCommission = 124; break;
                case 5: buyCommission = 284; break;
                default: break;
            }


            if (level == 0) {
                //用户第一次购买,要加上推荐成功的佣金
                switch (Math.min(pLevel, buyLevel)) {
                    case 1: recommCommission = 50; break;
                    case 2: recommCommission = 300; break;
                    case 3: recommCommission = 700; break;
                    case 4: recommCommission = 1500; break;
                    case 5: recommCommission = 3500; break;
                    default: break;
                }
            }

            totalCommission = buyCommission + recommCommission;


            //保存到数据库
            Map<String,Object> params = new HashMap<>();
            params.put("commission", totalCommission);
            params.put("userId", pUser.getId());
            int resule = commissionDao.addCommissionByUid(params);
            if (resule < 1) {
                logger.error("增加佣金失败，用户:" + pUser.getName() + "  佣金：" + totalCommission);
            }

            CommissionDetail commissionDetail = new CommissionDetail();
            commissionDetail.setCommission(totalCommission);
            commissionDetail.setCt(new Date());
            commissionDetail.setUserId(pUser.getId());
            commissionDetail.setComeby(user.getId());
            resule = commissionDetailDao.insertSelective(commissionDetail);
            if (resule < 1) {
                logger.error("增加佣金明细失败，用户:" + pUser.getName() + "  佣金：" + totalCommission);
            }
        }







        return true;
    }

}
