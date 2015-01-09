package com.ly.base.crawlproxy.proxy.crawl;

import com.tongcheng.lib.database.sqlserver.DBController;
import com.tongcheng.lib.database.sqlserver.DbUtil;

public class Test2 {
	public static void main(String[] args) {

		DBController db = DbUtil.getDbLink("TCUssCommon", "61.155.159.230:51158", "TCUssCommon",
				"ER$%TFE");

	}
}
