/**
 * Creation Date:2015年3月13日-下午3:35:07
 * 
 * Copyright 2008-2015 © 同程网 Inc. All Rights Reserved
 */
package com.ly.base.crawlproxy.proxy.util;

import com.tongcheng.lib.getpage.urlconn.HttpUtils;

/**
 * Description Of The Class<br/>
 * QQ:106193246
 * 
 * @author wang.zuode(王祚德)
 * @version 1.0.0, 2015年3月13日-下午3:35:07
 * @since 2015年3月13日-下午3:35:07
 */
public class LyUtil {
    public static void main(String[] args) {
        sendMessage("18020280367", "王祚德测试!!!");
    }
    
    /**
     * 发送监控短信-新接口，潘政给出,多个手机号之间用逗号隔开
     * 
     * @param mobile
     * @param cont
     */
    public static String sendMessage(String mobile, String cont) {
        // TCBase.Crawl TCBase.Crawl2250
        String url = "http://tccommon.17usoft.com/smstemplate/service/SendMessage?account=TCBase.Crawl&password=TCBase.Crawl2250&mobile=" + mobile + "&message=" + cont;
        String c = HttpUtils.getContent(url, "", 3);
        System.out.println(c);
        if (c.equals("")) {
            c = "失败，短信提交不成功！";
        }
        return c;
    }
}
