package com.payroll.report.repo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.payroll.report.model.PayrollStatement;

public interface PayrollRepo {

	public List<PayrollStatement> getPreviousMonthPayroll(String previousMonth) throws IOException, SQLException;;

	public List<PayrollStatement> getYTPPayroll(String startOfYearDate, String endOfYearDate)
			throws IOException, SQLException;

	List<PayrollStatement> getAllPayrolls() throws IOException, SQLException;

	public List<PayrollStatement> getCurrentMonthPayroll(String currentMonth) throws IOException, SQLException;;

	List<PayrollStatement> getPayrollsMonthWise(String month) throws IOException, SQLException;;
}
