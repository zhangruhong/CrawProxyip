package com.ly.base.crawlproxy.proxy.crawl;

/**
 * Creation Date:2013-7-17
 * 
 * Copyright 2008-2010 © 同程网 Inc. All Rights Reserved
 */

import java.sql.SQLException;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.tongcheng.lib.datetime.DateUtilZuode;
import com.tongcheng.lib.sleep.SleepWait;

/**
 * Description Of The Class<br>
 * Email:wzd@17u.com
 * 
 * @author zuode.wang(王祚德)
 * @version 1.000, 2013-7-17
 * 
 */
public class Del implements Runnable {
    
    private SqlMapClient sqlMapClient;
    
    public Del(SqlMapClient sqlMapClient) {
        // TODO Auto-generated constructor stub
        this.sqlMapClient = sqlMapClient;
    }
    
    public void delData() {
        /** 删除DELETE */
        try {
            // sqlMapClient.delete("deleteProxyIP", TimeUtil.getTimePoint(1));
            sqlMapClient.delete("deleteProxyIP", DateUtilZuode.getYesterday(30));
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (true) {
            delData();
            System.out.println("线上库中一小时的代理已经成功删除！！！");
            SleepWait.sleepHourTime(24);
        }
    }
}
