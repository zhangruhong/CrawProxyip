package com.ly.base.crawlproxy.proxy.dao;

/**
 * Creation Date:2013-7-17
 * 
 * Copyright 2008-2010 © 同程网 Inc. All Rights Reserved
 */

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ly.base.crawlproxy.proxy.bean.ProxyIP;
import com.tongcheng.lib.datetime.DateUtilZuode;

/**
 * Description Of The Class<br>
 * Email:wzd@17u.com
 * 
 * @author zuode.wang(王祚德)
 * @version 1.000, 2013-7-17
 * 
 */
public class ProxyIPDao {

	private SqlMapClient sqlMapClient;

	public ProxyIPDao(SqlMapClient sqlMapClient) {
		// TODO Auto-generated constructor stub
		this.sqlMapClient = sqlMapClient;
	}

	public boolean checkProxyExits(String ip, int port) {
		/** 查询FIND BY ID */
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("ip", ip);
		parms.put("port", port);
		ProxyIP proxy = null;
		try {
			proxy = (ProxyIP) sqlMapClient.queryForObject("getProxyIP", parms);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (null == proxy) {
			System.out.println("代理是新的，需要检测入库。。。");
			return true;
		} else {
			System.out.println("新获取的代理在库中已经存在。。。");
			return false;
		}
	}

	public synchronized void insertProxyIPData(String proxyip, int port) {
		/** 查询FIND BY ID */
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("ip", proxyip);
		parms.put("port", port);
		ProxyIP proxy = null;
		try {
			proxy = (ProxyIP) sqlMapClient.queryForObject("getProxyIP", parms);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (null == proxy) {
			/** 插入INSERT */
			ProxyIP proxydata = new ProxyIP(proxyip, port, "taobaobuy", "1", "高匿", "10", DateUtilZuode.DoFormatDateymdhms());
			try {
				sqlMapClient.insert("insertProxyIP", proxydata);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("新代理数据插入成功！");
		} else {
			System.out.println("代理库中已经存在该代理ip...");
			System.out.println("返回得到的: " + proxy.getIp());
		}
	}

	public void updateProxyIPData(String proxyip, int port) {
		/** 查询FIND BY ID */
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("ip", proxyip);
		parms.put("port", port);
		try {
			sqlMapClient.update("updateProxyIP", parms);
			System.out.println("代理库中已经存在该代理ip...标志成功更新！！！");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
