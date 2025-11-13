package com.payroll.report.job;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.payroll.report.util.ClientProperties;
import com.payroll.report.util.DatabaseUtil;

public class PayrollDBTruncateJob implements Job {
	private final static Logger LOG = LoggerFactory.getLogger(PayrollDBTruncateJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		LOG.info("PayrollDBTruncateJob is start executing : {}", Instant.now());
		String flag = null;
		try {
			flag = ClientProperties.getProperty("truncate.appuseraudit.job.flag").trim();
		} catch (IOException e) {
			LOG.error("IO Exception in the execute : {}", e.getMessage(), e);
		}
		Boolean jobFlag = Boolean.parseBoolean(flag);
		igniteJob(jobFlag);
		LOG.info("PayrollDBTruncateJob is executed : {}", Instant.now());
	}

	public static void igniteJob(boolean flag) {

		if (flag) {
			try {
				String jobName = "Del_One_Month_Old_App_User_Audit_Log_Job";
				Timestamp jobStartTime = Timestamp.from(Instant.now());
				DatabaseUtil.saveJobInDB(jobName, jobStartTime);
				DatabaseUtil.delOldUserAuditData();
			} catch (SQLException e) {
				LOG.error("SQL Exception in the igniteJob : {} ", e.getMessage(), e);
			}
		}
	}
}
