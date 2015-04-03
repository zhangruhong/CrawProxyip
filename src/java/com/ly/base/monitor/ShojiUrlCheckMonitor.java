/**
 * Creation Date:2015年4月3日-下午7:02:37
 * 
 * Copyright 2008-2015 © 同程网 Inc. All Rights Reserved
 */
package com.ly.base.monitor;

import org.apache.commons.lang3.StringUtils;

import com.ly.base.crawlproxy.proxy.util.LyUtil;
import com.tongcheng.lib.datetime.DateUtilZuode;
import com.tongcheng.lib.getpage.dywuss.GetPageSrc;
import com.tongcheng.lib.sleep.SleepWait;

/**
 * Description Of The Class<br/>
 * QQ:106193246
 * 
 * @author wang.zuode(王祚德)
 * @version 1.0.0, 2015年4月3日-下午7:02:37
 * @since 2015年4月3日-下午7:02:37
 */
public class ShojiUrlCheckMonitor {
    
    public static int failCount = 0;
    
    public static void main(String[] args) {
        while (true) {
            int x = crawlShouji();
            if (x == 1)
                SleepWait.sleepMinsTime(30);
            else
                SleepWait.sleepMinsTime(1);
            
        }
    }
    
    public static int crawlShouji() {
        String url = "http://v.showji.com/Locating/showji.com20150108.aspx?m=18914095862&output=json&callback=querycallback&timestamp=1425283243085";
        String c = GetPageSrc.getPageContent(url, 3);
        if (StringUtils.isNotBlank(c) && c.contains("querycallback")) {
            System.out.println(DateUtilZuode.DoFormatDateymdhms() + "   手机之家地址没有改变。。。。");
            failCount = 0;
        } else {
            failCount++;
            if (failCount > 10) {
                System.out.println(DateUtilZuode.DoFormatDateymdhms() + "   手机之家地址发生改变。。。。");
                LyUtil.sendMessage("18020280367", DateUtilZuode.DoFormatDateymdhms() + "   手机之家地址发生改变。。。。");
                return 1;
            }
        }
        return 0;
    }
    
}
