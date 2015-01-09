package com.ly.base.crawlproxy.jk;

/**
 * Creation Date:2011-3-25
 * 
 * Copyright 2008-2010 © 同程网 Inc. All Rights Reserved
 */

/**
 * Description Of The Class<br>
 * Email:wzd@17u.com
 * 
 * @author zuode.wang(王祚德)
 * @version 1.000, 2011-3-25
 * 
 */
import java.sql.ResultSet;
import java.util.Date;

import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import com.tongcheng.lib.database.sqlserver.DBController;
import com.tongcheng.lib.database.sqlserver.DbUtil;
import com.tongcheng.lib.datetime.DateUtilZuode;
import com.tongcheng.lib.sendmessage.SendInfo;

public class CheckJiPiaoStat2 implements Job, Runnable {

	public CheckJiPiaoStat2() {
	}

	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("开始调度执行....");
		System.out.println("In SimpleQuartzJob - executing its JOB at " + new Date() + " by "
				+ context.getTrigger().getName());
		DBController db = DbUtil.getDbLink("TCFly", "172.16.58.2", "sa", "sa3210.");
		String select = "select count(1) as s FROM [TCFly].[dbo].[HotAirLinedir] with(nolock) where updatetime>='"
				+ DateUtilZuode.DoFormatDateyyyymmdd2() + "'";
		System.out.println(select);
		try {
			db.executeQuery(select);
			ResultSet rs = db.getRs();
			if (rs.next()) {
				if (rs.getString("s").equals("0")) {
					SendInfo.sentMessage("15502179801", "jingao机票比价"
							+ DateUtilZuode.getDateTimeString("hh") + "时10:30的抓取量是"
							+ rs.getString("s"));
					SendInfo.sentMessage("18020280367", "jingao机票比价"
							+ DateUtilZuode.getDateTimeString("hh") + "时10:30的抓取量是"
							+ rs.getString("s"));
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.close();
	}

	public void task() throws SchedulerException {
		// Initiate a Schedule Factory
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		// Retrieve a scheduler from schedule factory
		Scheduler scheduler = schedulerFactory.getScheduler();

		// current time
		// long ctime = System.currentTimeMillis();

		// Initiate JobDetail with job name, job group, and executable job class
		JobDetail jobDetail = new JobDetail("jobDetail2", "jobDetailGroup", CheckJiPiaoStat2.class);
		// Initiate CronTrigger with its name and group name
		CronTrigger cronTrigger = new CronTrigger("cronTrigger2", "triggerGroup");
		try {
			// setup CronExpression
			// 0 04 12-16 ? * *
			// 0 33 16 * * ?
			CronExpression cexp = new CronExpression("0 30 10 * * ?");
			// Assign the CronExpression to CronTrigger
			cronTrigger.setCronExpression(cexp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// schedule a job with JobDetail and Trigger
		scheduler.scheduleJob(jobDetail, cronTrigger);

		// start the scheduler
		scheduler.start();
	}

	@Override
	public void run() {
		try {
			task();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Thread(new CheckJiPiaoStat2()).start();
	}
}