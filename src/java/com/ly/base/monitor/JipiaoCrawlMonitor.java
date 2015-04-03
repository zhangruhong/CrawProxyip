/**
 * Creation Date:2015年3月13日-下午2:45:18
 * 
 * Copyright 2008-2015 © 同程网 Inc. All Rights Reserved
 */
package com.ly.base.monitor;

/**
 * Description Of The Class<br/>
 * QQ:106193246
 * 
 * @author wang.zuode(王祚德)
 * @version 1.0.0, 2015年3月13日-下午2:45:18
 * @since 2015年3月13日-下午2:45:18
 */

import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ly.base.crawlproxy.proxy.util.LyUtil;
import com.tongcheng.lib.database.sqlserver.DBController;
import com.tongcheng.lib.database.sqlserver.DbUtil;
import com.tongcheng.lib.datetime.DateUtilZuode;

@Component
public class JipiaoCrawlMonitor {
    
    private static final Logger logger = LoggerFactory.getLogger(JipiaoCrawlMonitor.class);
    
    @Scheduled(cron = "0 30 10,11,12,13 * * ?")
    public void clientInfoTimer() {
        logger.info("开始调度执行....");
        System.out.println("开始调度执行....");
        System.out.println("In SimpleQuartzJob - executing its JOB at " + DateUtilZuode.DoFormatDateymdhms());
        DBController db = DbUtil.getDbLink("TCFly", "172.16.58.2", "sa", "sa3210.");
        String select = "select count(1) as s FROM [TCFly].[dbo].[HotAirLinedir] with(nolock) where updatetime>='" + DateUtilZuode.DoFormatDateyyyymmdd2() + "'";
        System.out.println(select);
        try {
            db.executeQuery(select);
            ResultSet rs = db.getRs();
            if (rs.next()) {
                
                LyUtil.sendMessage("18020280367", "机票比价" + DateUtilZuode.getDateTimeString("hh") + "时的抓取量是" + rs.getString("s"));
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        db.close();
        System.out.println("任务处理结束...");
    }
}