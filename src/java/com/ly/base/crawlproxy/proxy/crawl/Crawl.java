package com.ly.base.crawlproxy.proxy.crawl;

/**
 * Creation Date:2013-7-17
 * 
 * Copyright 2008-2010 © 同程网 Inc. All Rights Reserved
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ly.base.crawlproxy.proxy.util.FetchUtil;
import com.ly.base.crawlproxy.proxy.util.LyUtil;
import com.tongcheng.lib.getpage.dywuss.GetPageSrc;
import com.tongcheng.lib.sleep.SleepWait;
import com.usousou.crawl.hps.bean.HttpProxy;
import com.usousou.crawl.hps.service.IGetHttpProxyService;
import com.usousou.crawl.httpdownloader.bean.UssPageContent;
import com.usousou.crawl.httpdownloader.service.IPageContentDownService;
import com.usousou.crawl.httpdownloader.service.impl.PageContentDownServiceImpl;

/**
 * Description Of The Class<br>
 * Email:wzd@17u.com
 * 
 * @author zuode.wang(王祚德)
 * @version 1.000, 2013-7-17
 * 
 */
public class Crawl implements Runnable {
    
    private String[] crawlUrlList;
    private String[] ipList;// 公司ip列表
    private String SupplierName;// 淘宝供应商专家名称
    private boolean isValid;
    private int waitmin;// 等待多少秒再抓取
    
    /**
     * 
     * @param dao
     * @param m
     * @param b
     * @param ipList
     * @param SupplierName
     * @param isValid
     *            是否验证 true 验证代理
     */
    public Crawl(String[] urllist, String[] ipList, String SupplierName, boolean isValid, int waitmin) {
        // TODO Auto-generated constructor stub
        this.crawlUrlList = urllist;
        this.ipList = ipList;
        this.SupplierName = SupplierName;
        this.isValid = isValid;
        this.waitmin = waitmin;
    }
    
    @Override
    public void run() {
        // TODO Auto-generated method stub
        int count = 0;
        while (true) {
            count++;
            
            boolean issucc = false;
            
            for (String url: crawlUrlList) {
                
                boolean flag = crawlTaobao(count, url, isValid);
                
                // 抓取成功返回TRUE
                if (flag) {
                    issucc = true;
                    break;
                }
                
            }
            
            if (!issucc) {
                // SendInfo.sentMessage("18020280367", SupplierName +
                // "供应商代理抓取不到了!");
                LyUtil.sendMessage("18020280367", SupplierName + "供应商代理抓取不到了!");
                System.out.println("监控报警短信已经发出！");
                SleepWait.sleepMinsTime(waitmin);
            } else
                SleepWait.sleepSecondTime(1);
        }
    }
    
    public boolean crawlTaobao(int count, String url, boolean isCheck) {
        System.out.println(count + "-" + SupplierName + "   提取proxy:" + url);
        IPageContentDownService pageContentDownService = new PageContentDownServiceImpl();
        UssPageContent ussPageContent = pageContentDownService.getPageContent(url, IGetHttpProxyService.NO_PROXY, 30000, 30000, 5, null);
        boolean flag = false;
        if (null != ussPageContent) {
            
            String c = ussPageContent.getContent();// .replaceAll("\r|\n", "");
            
            String proxyip = "";
            String port = "";
            
            String patt = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})\\:(\\d+)";
            Matcher m = Pattern.compile(patt).matcher(c);
            boolean isMatched = m.find();
            System.out.println(isMatched);
            while (isMatched) {
                flag = true;
                count++;
                System.out.println(count + ">" + SupplierName + "提取到  " + m.group(1) + ":" + m.group(2));
                proxyip = m.group(1);
                port = m.group(2);
                
                if (isCheck) {
                    HttpProxy httpProxy = new HttpProxy();
                    httpProxy.setIp(proxyip);
                    httpProxy.setPort(Integer.parseInt(port));
                    // dao.insertProxyIPData(proxyip,
                    // Integer.parseInt(port));//无法验证的时候直接入库
                    
                    String content = FetchUtil.getContent(httpProxy, "http://httpproxy.17usoft.com/tcproxy/test/ip_a.jsp", 40000, 40000, 2, 2, "GBK", "<title>", "</center>");
                    if (null != content) {
                        System.out.println("content  " + content.replaceAll("\r|\n", "").trim());
                        System.out.println(httpProxy.getResponseTime());
                    }
                    if (null == content) {
                        System.out.println("代理验证网页请求失败！" + SupplierName);
                    } else if (null != content) {
                        boolean flag2 = true;
                        for (int i = 0; i < ipList.length; i++) {
                            if (content.contains(ipList[i])) {
                                System.out.println("该地址不符合要求！" + SupplierName);
                                flag2 = false;
                                break;
                            }
                        }
                        if (flag2) {
                            System.out.println("该地址符合要求！" + SupplierName);
                            System.out.println("调用保存接口");
                            GetPageSrc.getPageContent("http://httpproxy.17usoft.com/tcproxy/addProxy.do?ip=" + proxyip + "&port=" + port, 3);
                        }
                    }
                } else {
                    System.out.println("不要验证直接入库！" + SupplierName);
                    System.out.println("调用保存接口");
                    GetPageSrc.getPageContent("http://httpproxy.17usoft.com/tcproxy/addProxy.do?ip=" + proxyip + "&port=" + port, 3);
                }
                isMatched = m.find();
            }
        } else {
            return false;
        }
        return flag;
    }
}