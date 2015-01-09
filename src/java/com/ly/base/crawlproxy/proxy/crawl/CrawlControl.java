package com.ly.base.crawlproxy.proxy.crawl;

/**
 * Creation Date:2011-10-10
 * 
 * Copyright 2008-2010 © 同程网 Inc. All Rights Reserved
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.ly.base.crawlproxy.jk.CheckJiPiaoStat;
import com.ly.base.crawlproxy.proxy.dao.ProxyIPDao;

public class CrawlControl {

	public static boolean isRunning = true;

	static SqlMapClient sqlMapClient = null;
	static {
		try {
			Reader reader = Resources.getResourceAsReader("SqlMapConfig.xml");
			sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String[] initIPList() {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(
				"ipConfig.properties");
		Properties p = new Properties();
		try {
			p.load(inputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.out.println("公司ip列表:" + p.getProperty("ip"));
		return p.getProperty("ip").split(";");
	}

	public void crawlData() {

		isRunning = false;

		ProxyIPDao dao = new ProxyIPDao(sqlMapClient);

		String[] ipList = initIPList();

		boolean isValid = true;// false不验证

		String pu56 = "http://www.56pu.com/fetch?orderId=611049206579522&quantity=&line=all&region=&regionEx=&beginWith=&ports=&speed=&anonymity=&scheme=";
		new Thread(new Crawl(dao, pu56.split(","), ipList, "56pu", isValid, 20)).start();
		new Thread(new Crawl(dao, pu56.split(","), ipList, "56pu", isValid, 20)).start();
		new Thread(new Crawl(dao, pu56.split(","), ipList, "56pu", isValid, 20)).start();

		String[] iphais = new String[3];
		iphais[0] = "http://api.iphai.com/getapi.ashx?ddh=zuode520&num=100&yys=1&port=1998,18186,9999,8888,8080&am=0&mt=6&fm=text";
		iphais[1] = "http://net.iphai.com/getapi.ashx?ddh=zuode520&num=100&yys=1&port=1998,18186,9999,8888,8080&am=0&mt=6&fm=text";
		iphais[2] = "http://www.iphai.com/getapi.ashx?ddh=zuode520&num=100&yys=1&port=1998,18186,9999,8888,8080&am=0&mt=6&fm=text";

		// new Thread(new Crawl(dao, iphais, ipList, "iphai", isValid,
		// 20)).start();
		// new Thread(new Crawl(dao, iphais, ipList, "iphai", isValid,
		// 20)).start();
		// new Thread(new Crawl(dao, iphais, ipList, "iphai", isValid,
		// 20)).start();

		new Thread(new Del(sqlMapClient)).start();
		new Thread(new Check("http://httpproxy.17usoft.com/tcproxy/getProxy.do?count=1&min=10",
				"line")).start();

		new Thread(new CheckJiPiaoStat()).start();
		// new Thread(new CheckJiPiaoStat2()).start();
	}

	public static void main(String[] args) {
		new CrawlControl().crawlData();
	}

}
