package com.payroll.report.constant;

public class QueryConstant {

	public final static String ALLPAYROLLQUERY = "SELECT p.id, e.emp_code, e.name, p.payroll_month, p.basic_pay, p.hra, p.allowances, p.deductions, p.tax, p.overtime_hours, p.overtime_amount, p.bonus_amount, (p.basic_pay + p.hra + p.allowances + p.bonus_amount + p.overtime_amount) AS gross_pay, (p.basic_pay + p.hra + p.allowances + p.bonus_amount + p.overtime_amount) - (p.deductions + p.tax) AS net_pay, p.status FROM payroll_statement p JOIN employee e ON p.employee_id = e.id ORDER BY p.payroll_month DESC";
	public final static String MONTHWISEPAYROLLQUERY = "SELECT p.id, e.emp_code, e.name, p.payroll_month, p.basic_pay, p.hra, p.allowances, p.deductions, p.tax, p.overtime_hours, p.overtime_amount, p.bonus_amount, (p.basic_pay + p.hra + p.allowances + p.bonus_amount + p.overtime_amount) AS gross_pay, (p.basic_pay + p.hra + p.allowances + p.bonus_amount + p.overtime_amount) - (p.deductions + p.tax) AS net_pay, p.status FROM payroll_statement p JOIN employee e ON p.employee_id = e.id WHERE p.payroll_month >= to_date(?,'YYYY-MM') and  p.payroll_month < ADD_MONTHS(to_date(?,'YYYY-MM'),1)";
	public final static String USERMOBNOQUERY = "select MOBILE from app_user where email =?";
	public final static String CURRENTMONTHPAYROLLQUERY = "SELECT p.id, e.emp_code, e.name, p.payroll_month, p.basic_pay, p.hra, p.allowances, p.deductions, p.tax, p.overtime_hours, p.overtime_amount, p.bonus_amount, (p.basic_pay + p.hra + p.allowances + p.bonus_amount + p.overtime_amount) AS gross_pay, (p.basic_pay + p.hra + p.allowances + p.bonus_amount + p.overtime_amount) - (p.deductions + p.tax) AS net_pay, p.status FROM payroll_statement p JOIN employee e ON p.employee_id = e.id WHERE p.payroll_month >= to_date(?,'YYYY-MM') and  p.payroll_month < ADD_MONTHS(to_date(?,'YYYY-MM'),1)";
	public final static String PREVIOUSMONTHPAYROLLQUERY = "SELECT p.id, e.emp_code, e.name, p.payroll_month, p.basic_pay, p.hra, p.allowances, p.deductions, p.tax, p.overtime_hours, p.overtime_amount, p.bonus_amount, (p.basic_pay + p.hra + p.allowances + p.bonus_amount + p.overtime_amount) AS gross_pay, (p.basic_pay + p.hra + p.allowances + p.bonus_amount + p.overtime_amount) - (p.deductions + p.tax) AS net_pay, p.status FROM payroll_statement p JOIN employee e ON p.employee_id = e.id WHERE p.payroll_month >= to_date(?,'YYYY-MM') and  p.payroll_month < ADD_MONTHS(to_date(?,'YYYY-MM'),1)";
	public final static String YTPPAYROLLQUERY = "SELECT p.id, e.emp_code, e.name, p.payroll_month, p.basic_pay, p.hra, p.allowances, p.deductions, p.tax, p.overtime_hours, p.overtime_amount, p.bonus_amount, (p.basic_pay + p.hra + p.allowances + p.bonus_amount + p.overtime_amount) AS gross_pay, (p.basic_pay + p.hra + p.allowances + p.bonus_amount + p.overtime_amount) - (p.deductions + p.tax) AS net_pay, p.status FROM payroll_statement p JOIN employee e ON p.employee_id = e.id WHERE p.payroll_month >= to_date(?,'YYYY-MM-DD') and  p.payroll_month <= to_date(?,'YYYY-MM-DD')";

	public final static String USERTYPEQUERY = "select role from app_user where email=?";
	public final static String DEL_APPUSER_AUDIT_QUERY = "DELETE FROM app_user_login_audit WHERE LAST_LOGIN_DTTM <ADD_MONTHS(SYSDATE, -1)";

	private QueryConstant()

	{

	}
}
