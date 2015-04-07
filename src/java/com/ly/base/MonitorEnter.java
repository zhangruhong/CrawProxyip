/**
 * Creation Date:2015年3月13日-下午2:49:44
 * 
 * Copyright 2008-2015 © 同程网 Inc. All Rights Reserved
 */
package com.ly.base;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ly.base.crawlproxy.proxy.crawl.CrawlControl;
import com.ly.base.monitor.ProxyFetchCheck;

/**
 * Description Of The Class<br/>
 * QQ:106193246
 * 
 * @author wang.zuode(王祚德)
 * @version 1.0.0, 2015年3月13日-下午2:49:44
 * @since 2015年3月13日-下午2:49:44
 */
public class MonitorEnter implements Runnable {
    public static void main(String[] args) {
        new Thread(new MonitorEnter()).start();
    }
    
    @Override
    public void run() {
        // TODO Auto-generated method stub
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
        
        new CrawlControl().crawlData();
        
        new Thread(new ProxyFetchCheck("http://httpproxy.17usoft.com/tcproxy/getProxy.do?count=1&min=10", "linewzd-")).start();
        
    }
}
