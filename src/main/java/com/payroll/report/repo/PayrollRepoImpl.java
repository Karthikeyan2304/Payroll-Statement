package com.payroll.report.repo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.payroll.report.constant.QueryConstant;
import com.payroll.report.constant.DBType;
import com.payroll.report.db.connection.DBFactory;
import com.payroll.report.model.PayrollStatement;

public class PayrollRepoImpl implements PayrollRepo {
	private static final Logger LOG = LoggerFactory.getLogger(PayrollRepoImpl.class);

	@Override
	public List<PayrollStatement> getAllPayrolls() throws IOException, SQLException {
		List<PayrollStatement> list = new ArrayList<>();
		String sql = QueryConstant.ALLPAYROLLQUERY;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DBFactory.getConnection(DBType.ORACLE.name()).getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				PayrollStatement p = new PayrollStatement();
				p.setId(rs.getLong("id"));
				p.setEmpCode(rs.getString("emp_code"));
				p.setName(rs.getString("name"));
				p.setPayrollMonth(rs.getDate("payroll_month"));
				p.setBasicPay(rs.getBigDecimal("basic_pay"));
				p.setHra(rs.getBigDecimal("hra"));
				p.setAllowances(rs.getBigDecimal("allowances"));
				p.setDeductions(rs.getBigDecimal("deductions"));
				p.setTax(rs.getBigDecimal("tax"));
				p.setOvertimeHours(rs.getInt("overtime_hours"));
				p.setOvertimeAmount(rs.getBigDecimal("overtime_amount"));
				p.setBonusAmount(rs.getBigDecimal("bonus_amount"));
				p.setGrossPay(rs.getBigDecimal("gross_pay"));
				p.setNetPay(rs.getBigDecimal("net_pay"));
				p.setStatus(rs.getString("status"));
				list.add(p);
			}
		} catch (SQLException e) {
			LOG.error("SQLException in the getAllPayrolls {} : ",e.getMessage(),e);
		} finally {
			DBFactory.getConnection(DBType.ORACLE.name()).closeConnection(con, ps, rs);
		}
		return list;
	}

	@Override
	public List<PayrollStatement> getPayrollsMonthWise(String month) throws IOException, SQLException {
		List<PayrollStatement> list = new ArrayList<PayrollStatement>();
		String sql = QueryConstant.MONTHWISEPAYROLLQUERY;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DBFactory.getConnection(DBType.ORACLE.name()).getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, month.trim());
			ps.setString(2, month.trim());
			rs = ps.executeQuery();
			while (rs.next()) {
				PayrollStatement p = new PayrollStatement();
				p.setId(rs.getLong("id"));
				p.setEmpCode(rs.getString("emp_code"));
				p.setName(rs.getString("name"));
				p.setPayrollMonth(rs.getDate("payroll_month"));
				p.setBasicPay(rs.getBigDecimal("basic_pay"));
				p.setHra(rs.getBigDecimal("hra"));
				p.setAllowances(rs.getBigDecimal("allowances"));
				p.setDeductions(rs.getBigDecimal("deductions"));
				p.setTax(rs.getBigDecimal("tax"));
				p.setOvertimeHours(rs.getInt("overtime_hours"));
				p.setOvertimeAmount(rs.getBigDecimal("overtime_amount"));
				p.setBonusAmount(rs.getBigDecimal("bonus_amount"));
				p.setGrossPay(rs.getBigDecimal("gross_pay"));
				p.setNetPay(rs.getBigDecimal("net_pay"));
				p.setStatus(rs.getString("status"));
				list.add(p);
			}
		} catch (SQLException e) {
			LOG.error("SQLException in the getPayrollsMonthWise {} : ",e.getMessage(),e);
		} finally {
			DBFactory.getConnection(DBType.ORACLE.name()).closeConnection(con, ps, rs);
		}
		return list;
	}

	public List<PayrollStatement> getCurrentMonthPayroll(String currentMonth) throws SQLException {
		List<PayrollStatement> list = new ArrayList<>();
		String sql = QueryConstant.CURRENTMONTHPAYROLLQUERY;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DBFactory.getConnection("ORACLE").getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, currentMonth.trim());
			ps.setString(2, currentMonth.trim());
			rs = ps.executeQuery();
			while (rs.next()) {
				PayrollStatement p = new PayrollStatement();
				p.setId(rs.getLong("id"));
				p.setEmpCode(rs.getString("emp_code"));
				p.setName(rs.getString("name"));
				p.setPayrollMonth(rs.getDate("payroll_month"));
				p.setBasicPay(rs.getBigDecimal("basic_pay"));
				p.setHra(rs.getBigDecimal("hra"));
				p.setAllowances(rs.getBigDecimal("allowances"));
				p.setDeductions(rs.getBigDecimal("deductions"));
				p.setTax(rs.getBigDecimal("tax"));
				p.setOvertimeHours(rs.getInt("overtime_hours"));
				p.setOvertimeAmount(rs.getBigDecimal("overtime_amount"));
				p.setBonusAmount(rs.getBigDecimal("bonus_amount"));
				p.setGrossPay(rs.getBigDecimal("gross_pay"));
				p.setNetPay(rs.getBigDecimal("net_pay"));
				p.setStatus(rs.getString("status"));
				list.add(p);
			}
		} catch (SQLException e) {
			LOG.error("SQLException in the getCurrentMonthPayroll {} : ",e.getMessage(),e);
		} finally {
			DBFactory.getConnection(DBType.ORACLE.name()).closeConnection(con, ps, rs);
		}
		return list;
	}

	public List<PayrollStatement> getPreviousMonthPayroll(String currentMonth) throws SQLException {
		List<PayrollStatement> list = new ArrayList<>();
		String sql = QueryConstant.PREVIOUSMONTHPAYROLLQUERY;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DBFactory.getConnection(DBType.ORACLE.name()).getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, currentMonth.trim());
			ps.setString(2, currentMonth.trim());
			rs = ps.executeQuery();
			while (rs.next()) {
				PayrollStatement p = new PayrollStatement();
				p.setId(rs.getLong("id"));
				p.setEmpCode(rs.getString("emp_code"));
				p.setName(rs.getString("name"));
				p.setPayrollMonth(rs.getDate("payroll_month"));
				p.setBasicPay(rs.getBigDecimal("basic_pay"));
				p.setHra(rs.getBigDecimal("hra"));
				p.setAllowances(rs.getBigDecimal("allowances"));
				p.setDeductions(rs.getBigDecimal("deductions"));
				p.setTax(rs.getBigDecimal("tax"));
				p.setOvertimeHours(rs.getInt("overtime_hours"));
				p.setOvertimeAmount(rs.getBigDecimal("overtime_amount"));
				p.setBonusAmount(rs.getBigDecimal("bonus_amount"));
				p.setGrossPay(rs.getBigDecimal("gross_pay"));
				p.setNetPay(rs.getBigDecimal("net_pay"));
				p.setStatus(rs.getString("status"));
				list.add(p);
			}
		} catch (SQLException e) {
			LOG.error("SQLException in the getPreviousMonthPayroll {} : ",e.getMessage(),e);
		} finally {
			DBFactory.getConnection(DBType.ORACLE.name()).closeConnection(con, ps, rs);
		}
		return list;
	}

	public List<PayrollStatement> getYTPPayroll(String startOfYearDate, String endOfYearDate) throws SQLException {
		List<PayrollStatement> list = new ArrayList<>();
		String sql = QueryConstant.YTPPAYROLLQUERY;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DBFactory.getConnection(DBType.ORACLE.name()).getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, startOfYearDate.trim());
			ps.setString(2, endOfYearDate.trim());
			rs = ps.executeQuery();
			while (rs.next()) {
				PayrollStatement p = new PayrollStatement();
				p.setId(rs.getLong("id"));
				p.setEmpCode(rs.getString("emp_code"));
				p.setName(rs.getString("name"));
				p.setPayrollMonth(rs.getDate("payroll_month"));
				p.setBasicPay(rs.getBigDecimal("basic_pay"));
				p.setHra(rs.getBigDecimal("hra"));
				p.setAllowances(rs.getBigDecimal("allowances"));
				p.setDeductions(rs.getBigDecimal("deductions"));
				p.setTax(rs.getBigDecimal("tax"));
				p.setOvertimeHours(rs.getInt("overtime_hours"));
				p.setOvertimeAmount(rs.getBigDecimal("overtime_amount"));
				p.setBonusAmount(rs.getBigDecimal("bonus_amount"));
				p.setGrossPay(rs.getBigDecimal("gross_pay"));
				p.setNetPay(rs.getBigDecimal("net_pay"));
				p.setStatus(rs.getString("status"));
				list.add(p);
			}
		} catch (SQLException e) {
			LOG.error("SQLException in the getYTPPayroll {} : ",e.getMessage(),e);
		} finally {
			DBFactory.getConnection(DBType.ORACLE.name()).closeConnection(con, ps, rs);
		}
		return list;
	}

	public boolean getUserType(String userName) throws SQLException {
		boolean adminRole = false;
		String roleType = null;
		if (StringUtils.isNoneBlank(userName)) {

			String sql = QueryConstant.USERTYPEQUERY;
			Connection con = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				con = DBFactory.getConnection(DBType.ORACLE.name()).getConnection();
				ps = con.prepareStatement(sql);
				ps.setString(1, userName.trim());
				rs = ps.executeQuery();
				while (rs.next()) {
					roleType = rs.getString("role");
				}
				if (StringUtils.isNoneBlank(roleType)) {
					if (StringUtils.equalsAnyIgnoreCase(roleType, "ADMIN")) {
						adminRole = true;
					} else {
						adminRole = false;
					}
				}
			} catch (SQLException e) {
				
				LOG.error("SQLException in the getUserType {} : ",e.getMessage(),e);
			} finally {
				DBFactory.getConnection(DBType.ORACLE.name()).closeConnection(con, ps, rs);
			}

		}
		return adminRole;

	}

	@Override
	public Connection getConnection() {
		// TODO Auto-generated method stub
		return null;
	}

}
