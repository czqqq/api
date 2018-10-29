package com.api.tasks;

import com.api.dao.UserDao;
import com.api.model.User;
import com.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class CommissionTasks {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Scheduled(cron = "0 0 1 * * *")  //（秒 分 时 每月第几天 月 星期） //todo 测试用 0 0/1 * * * * 每分钟执行一次  正式运行用 0 0 1 * * *每天凌晨1点执行
    public void calcCommission() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info(sdf.format(new Date()) +" 佣金统计任务开始...");

        List<User> userList = userService.getAll();
        for (User user : userList) {
            int level = user.getLevel();
            if (level == 0) {
                continue;
            }
            double commission = 0; //每天增加的佣金
            double reappearance = 0; //每天返现

            //步骤1 按照级别计算每天返现
            switch (level){
                case 1: reappearance = 8;    break;
                case 2: reappearance = 300;  break;
                case 3: reappearance = 700;  break;
                case 4: reappearance = 1600; break;
                default: break;
            }

            commission += reappearance;
            logger.info(user.getName()+"的佣金为：" + commission);
        }








        logger.info(sdf.format(new Date()) +" 佣金统计任务结束...");
    }
}
