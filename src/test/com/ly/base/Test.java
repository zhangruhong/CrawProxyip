/**
 * Creation Date:2013-7-17
 * 
 * Copyright 2008-2010 © 同程网 Inc. All Rights Reserved
 */
package com.ly.base;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.ly.base.crawlproxy.proxy.bean.ProxyIP;
import com.ly.base.crawlproxy.proxy.util.TimeUtil;
import com.tongcheng.lib.datetime.DateUtilZuode;

/**
 * author:you.me 2010-12-22
 * 
 */
public class Test {
	static SqlMapClient sqlMapClient = null;
	static {
		try {
			Reader reader = Resources.getResourceAsReader("SqlMapConfig.xml");
			sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException, SQLException {
		/** 查询FIND BY ID */
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("ip", "201.152.36.315");
		parms.put("port", 8080);
		ProxyIP proxy = (ProxyIP) sqlMapClient.queryForObject("getProxyIP", parms);
		if (null != proxy)
			System.out.println("返回得到的: " + proxy.getIp());

		if (null == proxy) {
			/** 插入INSERT */
			proxy = new ProxyIP("201.152.36.35", 8080, "taobaoarea", "1", "高匿", "10", DateUtilZuode.DoFormatDateymdhms());
			sqlMapClient.insert("insertProxyIP", proxy);
		}

		/** 删除DELETE */
		sqlMapClient.delete("deleteProxyIP", TimeUtil.getTimePoint(1));

	}
}
