package com.ly.base.crawlproxy.jk;

/**
 * Creation Date:2014-3-14
 * 
 * Copyright 2008-2010 © 同程网 Inc. All Rights Reserved
 */

import com.ly.base.crawlproxy.proxy.util.HttpClientExample;
import com.ly.base.crawlproxy.proxy.util.LyUtil;
import com.tongcheng.lib.sleep.SleepWait;

/**
 * Description Of The Class<br>
 * Email:wzd@17u.com
 * 
 * @author zuode.wang(王祚德)
 * @version 1.000, 2014-3-14
 * 
 */
public class CheckProxyCrawl implements Runnable {
    
    private String url;
    
    private String name;
    
    public CheckProxyCrawl(String url, String name) {
        // TODO Auto-generated constructor stub
        this.url = url;
        this.name = name;
    }
    
    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (true) {
            System.out.println("检测中。。。。");
            String source = HttpClientExample.getGetResponseWithHttpClient(url, "GBK");
            if (null != source && source.replaceAll("\r|\n", "").trim().contains(" cxf proxycrawl system!")) {
                System.out.println("代理接口可以正常提取数据");
            } else {
                LyUtil.sendMessage("18020280367", name + "cxf 代理proxycrawl服务挂了!");
                System.out.println("监控报警短信已经发出！");
                SleepWait.sleepMinsTime(20);
            }
            System.out.println(source);
            SleepWait.sleepSecondTime(30);
        }
    }
    
    public static void main(String[] args) {
        new Thread(new CheckProxyCrawl("http://172.16.58.2:8180/proxycrawl/", "line")).start();
    }
    
}
