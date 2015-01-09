package com.ly.base.crawlproxy.proxy.crawl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tongcheng.lib.datetime.DateUtilZuode;
import com.usousou.crawl.hps.service.IGetHttpProxyService;
import com.usousou.crawl.httpdownloader.bean.UssPageContent;
import com.usousou.crawl.httpdownloader.service.IPageContentDownService;
import com.usousou.crawl.httpdownloader.service.impl.PageContentDownServiceImpl;

public class Test {
	public static void main(String[] args) {
		System.out.println(DateUtilZuode.getYesterday(30));
		System.exit(0);
		IPageContentDownService pageContentDownService = new PageContentDownServiceImpl();
		UssPageContent ussPageContent = pageContentDownService.getPageContent("http://www.iphai.com/getapi.ashx?ddh=zuode520&num=100&yys=1&port=1998,18186,9999,8888,8080&am=0&mt=6&fm=text", IGetHttpProxyService.NO_PROXY, 30000, 30000, 5, null);
		boolean flag = true;
		if (null != ussPageContent) {

			String c = ussPageContent.getContent();// .replaceAll("\r|\n", "");

			int count = 0;

			String patt = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})\\:(\\d+)";
			Matcher m = Pattern.compile(patt).matcher(c);
			boolean isMatched = m.find();
			System.out.println(isMatched);
			while (isMatched) {
				count++;
				System.out.print(count + "   提取到  " + m.group(1) + ":   ");
				System.out.println(m.group(2));
				isMatched = m.find();
			}
		}
	}
}
