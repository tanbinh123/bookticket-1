package com.ma.bookticket.config;

import com.ma.bookticket.service.TripsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


/**
 * 定时任务
 *
 * @author yong
 * @date 2021/1/29 17:15
 */
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@EnableAsync        // 3.开启多线程
public class TaskConfig {

    @Autowired
    private TripsService tripsService;

    /**
     * 每天0点对超过发车日期的车次进行逻辑删除
     * @author yong
     * @date 2021/1/29 20:07
     * @return void
     */

    @Async
    @Scheduled(cron = "0 14 0 * * ?")
    public void trips_delte_task() {
        int count=0;        //记录删除条数
        count=tripsService.deleteEveryDay();
        if(count!=0)
            System.out.println("成功删除"+count+"条超时车次");
        else
            System.out.println("------------------------无定时任务需操作-------------------------");
    }

}
