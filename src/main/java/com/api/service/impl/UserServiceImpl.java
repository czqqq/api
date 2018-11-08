package com.api.service.impl;

import com.api.dao.*;
import com.api.model.*;
import com.api.service.UserService;
import com.github.pagehelper.PageHelper;
import org.apache.commons.codec.language.bm.Languages;
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
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private TokenDao tokenDao;

    @Override
    public User getUserById(Long id) {
        return userDao.selectById(id);
    }

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
    public boolean addParentCommission(Long userId, double price) {
        User user = getUserById(userId);
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
            double buyCommission=0, recommCommission=0,totalCommission;
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

            Commission commission = commissionDao.selectById(pUser.getId());
            if (commission == null) {
                commission = new Commission();
                commission.setUserId(pUser.getId());
                commission.setCommission(0.0);
                commission.setStatus((byte)0);
                commissionDao.insertSelective(commission);
            }

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
            commissionDetail.setMark("用户:"+user.getName() + " 购买产生的佣金，共:" + totalCommission);
            resule = commissionDetailDao.insertSelective(commissionDetail);
            if (resule < 1) {
                logger.error("增加佣金明细失败，用户:" + pUser.getName() + "  佣金：" + totalCommission);
            }
        }

        return true;
    }

    @Override
    public int calcUserLevel(Long uid) {
        if (uid == null) {
            logger.error("用户ID不能为空");
            return 0;
        }
        int result = 0;

        User user = userDao.selectById(uid);
        int nowLevel = user.getLevel();

        Order o = new Order();
        o.setUserId(uid);
        o.setStatus((byte)1);
        List<Order> orders = orderDao.selectByEntity(o);
        if (orders != null && orders.size() > 0) {
            Order lastOrder = orders.get(0);
            //最后一次购买的金额
            double lastPrice = lastOrder.getTotalPrice();

            //之前的总消费金额
            double consumption = 0.0;
            for (int i = 0; i < orders.size(); i++) {
                if (orders.get(i).getTotalPrice() > 800) {
                    //购买800 以上的产品才进行累计升级
                    consumption += orders.get(i).getTotalPrice();
                }
            }

            //根据购买的产品判断等级是否变更
            if (lastPrice >= 800.0) {//购买最低级别的产品不进行升级
                int buyLevel = 0, addupLevel = 0; //计算这次买的等级和累计的等级
                switch ((int)lastPrice) {
                    case 800 : buyLevel = 1; break;
                    case 6400 : buyLevel = 2; break;
                    case 12800 : buyLevel = 3; break;
                    case 24800 : buyLevel = 4; break;
                    case 56800 : buyLevel = 5; break;
                    default: break;
                }

                switch ((int)consumption) { //todo 用 if else
                    case 800 : addupLevel = 1; break;
                    case 6400 : addupLevel = 2; break;
                    case 12800 : addupLevel = 3; break;
                    case 24800 : addupLevel = 4; break;
                    case 56800 : addupLevel = 5; break;
                    default: break;
                }

                int newLevel = Math.max(nowLevel, Math.max(buyLevel,addupLevel));
                if(newLevel != nowLevel){
                    User userNew = new User();
                    userNew.setId(user.getId());
                    userNew.setLevel(newLevel);
                    result = userDao.updateSelective(userNew);
                    if (result > 0) {
                        logger.info("用户： "+user.getName()+" 更新用户等级成功,升级为 " + newLevel + " 级用户");
                    }
                }
            }


        }
        return result;
    }

    @Override
    public int signToken(Long userId, String token) {


        Token token1 = tokenDao.selectById(token);
        if (token1 == null) {
            token1 = new Token();
            token1.setToken(token);
            token1.setUserId(userId);
            return tokenDao.insertSelective(token1);
        }else {
            return 1;
        }
    }

    @Override
    public int invalidToken(Long userId) {
        return tokenDao.deleteByUserId(userId);
    }

    @Override
    public int deleteTokenById(String token) {
        return tokenDao.deleteById(token);
    }

    @Override
    public Token getTokenbyId(String token) {
        return tokenDao.selectById(token);
    }

    @Override
    public int saveUserDailyCommission(User user, Double commission) {
        Map<String,Object> params = new HashMap<>();
        params.put("commission", commission);
        params.put("userId", user.getId());
        int resule = commissionDao.addCommissionByUid(params);
        if (resule < 1) {
            logger.error("增加佣金失败，用户:" + user.getName() + "  佣金：" + commission);
        }else{
            CommissionDetail commissionDetail = new CommissionDetail();
            commissionDetail.setCommission(commission);
            commissionDetail.setCt(new Date());
            commissionDetail.setUserId(user.getId());
            commissionDetail.setComeby(user.getId());
            commissionDetail.setMark("用户:"+user.getName() + " 每日返现，共:" + commission);
            resule = commissionDetailDao.insertSelective(commissionDetail);
            if (resule < 1) {
                logger.error("增加佣金明细失败，用户:" + user.getName() + "  佣金：" + commission);
            }
        }

        return resule;
    }


}
