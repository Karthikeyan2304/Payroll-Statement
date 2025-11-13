package com.payroll.report.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.payroll.report.model.PayrollStatement;
import com.payroll.report.repo.PayrollRepoImpl;

public class PayrollService {


	public static PayrollRepoImpl pay;
	static {
		pay = new PayrollRepoImpl();
	}



	public List<PayrollStatement> getAllPayrolls(String username, String passWord) throws IOException, SQLException {
		List<PayrollStatement> al = new ArrayList<PayrollStatement>();
		al = pay.getAllPayrolls();
		return al;
	}

	public List<PayrollStatement> getPayrollsMonthWise(String month) throws IOException, SQLException {
		List<PayrollStatement> al = new ArrayList<PayrollStatement>();
		if (StringUtils.isNotBlank(month)) {
			al = pay.getPayrollsMonthWise(month);
		}
		return al;
	}

	public List<PayrollStatement> getPayrollsExcelData(String month) throws IOException, SQLException {
		List<PayrollStatement> al = new ArrayList<PayrollStatement>();
		if (StringUtils.isNotBlank(month)) {
			al = pay.getPayrollsMonthWise(month);
		}
		return al;
	}

	public List<PayrollStatement> getCurrentMonthPayroll(String currentMonth) throws IOException, SQLException {
		List<PayrollStatement> al = new ArrayList<PayrollStatement>();
		if (StringUtils.isNotBlank(currentMonth)) {
			al = pay.getCurrentMonthPayroll(currentMonth);
		}
		return al;

	}

	public List<PayrollStatement> getPreviousMonthPayroll(String previousMonth) throws IOException, SQLException {
		List<PayrollStatement> al = new ArrayList<PayrollStatement>();
		if (StringUtils.isNotBlank(previousMonth)) {
			al = pay.getPreviousMonthPayroll(previousMonth);
		}
		return al;
	}

	public List<PayrollStatement> getYTPPayroll(String startOfYearDate, String endOfYearDate)
			throws IOException, SQLException {
		List<PayrollStatement> al = new ArrayList<PayrollStatement>();
		if (StringUtils.isNotBlank(startOfYearDate) && StringUtils.isNotBlank(endOfYearDate)) {
			al = pay.getYTPPayroll(startOfYearDate, endOfYearDate);
		}
		return al;

	}

}
