package com.ly.base.monitor;

/**
 * Creation Date:2014-3-14
 * 
 * Copyright 2008-2010 © 同程网 Inc. All Rights Reserved
 */

import org.apache.commons.lang3.StringUtils;

import com.tongcheng.lib.getpage.dywuss.GetPageSrc;
import com.tongcheng.lib.sendmessage.LyUtil;
import com.tongcheng.lib.sleep.SleepWait;

/**
 * Description Of The Class<br>
 * Email:wzd@17u.com
 * 
 * @author zuode.wang(王祚德)
 * @version 1.000, 2014-3-14
 * 
 */
public class ProxyFetchCheck implements Runnable {
    
    private String url;
    
    private String name;
    
    public ProxyFetchCheck(String url, String name) {
        // TODO Auto-generated constructor stub
        this.url = url;
        this.name = name;
    }
    
    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (true) {
            System.out.println("检测中。。。。");
            // String source =
            // HttpClientExample.getGetResponseWithHttpClient(url, "utf-8");
            String source = GetPageSrc.getPageContent(url, 3);
            source = StringUtils.strip(source);
            if (null != source && source.replaceAll("\r|\n", "").trim().matches("\\d+.\\d+.\\d+.\\d+\\:\\d+")) {
                System.out.println("代理接口可以正常提取数据");
                // LyUtil.sendMessage("18020280367", "代理接口可以正常提取数据");
            } else {
                // SendInfo.sentMessage("18020280367", name +
                // ">公司代理接口提取不到代理数据了，代理服务挂了!");
                LyUtil.sendMessage("18020280367", name + " > 公司代理接口提取不到代理数据了，代理服务挂了!");
                System.out.println("监控报警短信已经发出！");
                SleepWait.sleepMinsTime(5);
            }
            System.out.println(source);
            SleepWait.sleepSecondTime(30);
        }
    }
}
