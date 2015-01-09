package com.ly.base.crawlproxy.proxy.util;

/**
 * Creation Date:2013-7-17
 * 
 * Copyright 2008-2010 © 同程网 Inc. All Rights Reserved
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.tongcheng.lib.datetime.DateUtilZuode;

/**
 * Description Of The Class<br>
 * Email:wzd@17u.com
 * 
 * @author zuode.wang(王祚德)
 * @version 1.000, 2013-7-17
 * 
 */
public class TimeUtil {

	public static String getMin(int n) {
		String result = "";
		Calendar now = Calendar.getInstance();
		now.roll(java.util.Calendar.DAY_OF_YEAR, +(-n));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		result = sdf.format(now.getTime());
		System.out.println(result);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		// System.out.println(calendar.get(Calendar.DAY_OF_MONTH));//
		// 今天的日期
		calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - n);// 让日期加1
		// System.out.println(calendar.get(Calendar.DATE));//
		// 加1之后的日期Top

		// System.out.println(sdf.format(calendar.getTime()));
		result = sdf.format(calendar.getTime());

		return result;

	}

	/**
	 * 得到该时间点的前几小时时间信息
	 * 
	 * @param hours
	 * @return
	 */
	public static String getTimePoint(int hours) {
		String nowTime = DateUtilZuode.DoFormatDateymdhms();
		System.out.println(nowTime);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -hours);
		Date time = cal.getTime();
		// System.out.println(time.getTime());

		SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ZUOTIAN = sp.format(time);// 获取昨天日期

		System.out.println(ZUOTIAN);

		return ZUOTIAN;
	}
}
