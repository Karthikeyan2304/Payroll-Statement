package com.payroll.report.listener;

import java.io.IOException;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.payroll.report.job.PayrollDBTruncateJob;
import com.payroll.report.util.ClientProperties;

public class PayrollDBTruncateListener implements ServletContextListener {

	private static final Logger LOG = LoggerFactory.getLogger(PayrollDBTruncateListener.class);
	private Scheduler scheduler7;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			LOG.info("Quartz CronJob ContextInitialized");

			scheduler7 = StdSchedulerFactory.getDefaultScheduler();

			String cronExpression = ClientProperties.getProperty("appuser_truncate_cron_expression");
			LOG.info("appuser_truncate_cron_expression : {}", cronExpression);

			JobDetail job1 = JobBuilder.newJob(PayrollDBTruncateJob.class).withIdentity("myJob7", "defaultGroup")
					.build();

			Trigger trigger1 = TriggerBuilder.newTrigger().withIdentity("myTrigger7", "defaultGroup")
					.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();

			scheduler7.scheduleJob(job1, trigger1);
			scheduler7.start();
			LOG.info("Quartz Scheduler started");

		} catch (SchedulerException | IOException e) {
			LOG.error("SchedulerException: {}", e.getMessage(), e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// 1. Stop Quartz scheduler
		try {
			if (scheduler7 != null && !scheduler7.isShutdown()) {
				scheduler7.shutdown(true); // wait for jobs to finish
				LOG.info("Quartz Scheduler stopped");
			}
		} catch (SchedulerException e) {
			LOG.error("SchedulerException: {}", e.getMessage(), e);
		}

		// 2. Stop Log4j2 to avoid ThreadLocal memory leaks
		LoggerContext context = (LoggerContext) LogManager.getContext(false);
		context.stop();
		LOG.info("Log4j2 LoggerContext stopped");

		// 3. Deregister JDBC drivers to prevent memory leaks
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements()) {
			Driver driver = drivers.nextElement();
			try {
				DriverManager.deregisterDriver(driver);
				LOG.info("Deregistered JDBC driver: {}", driver);
			} catch (SQLException ex) {
				LOG.error("Error deregistering driver {}: {}", driver, ex.getMessage(), ex);
			}
		}
	}
}
