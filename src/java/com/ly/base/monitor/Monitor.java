/**
 * Creation Date:2015年3月13日-下午2:49:44
 * 
 * Copyright 2008-2015 © 同程网 Inc. All Rights Reserved
 */
package com.ly.base.monitor;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Description Of The Class<br/>
 * QQ:106193246
 * 
 * @author wang.zuode(王祚德)
 * @version 1.0.0, 2015年3月13日-下午2:49:44
 * @since 2015年3月13日-下午2:49:44
 */
public class Monitor implements Runnable {
    public static void main(String[] args) {
        new Thread(new Monitor()).start();
    }
    
    @Override
    public void run() {
        // TODO Auto-generated method stub
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
    }
}
