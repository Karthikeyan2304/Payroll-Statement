package com.payroll.report.model;

import java.math.BigDecimal;
import java.sql.Date;

public class PayrollStatement {

	private long id;
	private String empCode;
	private String name;
	private Date payrollMonth;
	private BigDecimal basicPay;
	private BigDecimal hra;
	private BigDecimal allowances;
	private BigDecimal deductions;
	private BigDecimal tax;
	private int overtimeHours;
	private BigDecimal overtimeAmount;
	private BigDecimal bonusAmount;
	private BigDecimal grossPay;
	private BigDecimal netPay;
	private String status;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getPayrollMonth() {
		return payrollMonth;
	}

	public void setPayrollMonth(Date payrollMonth) {
		this.payrollMonth = payrollMonth;
	}

	public BigDecimal getBasicPay() {
		return basicPay;
	}

	public void setBasicPay(BigDecimal basicPay) {
		this.basicPay = basicPay;
	}

	public BigDecimal getHra() {
		return hra;
	}

	public void setHra(BigDecimal hra) {
		this.hra = hra;
	}

	public BigDecimal getAllowances() {
		return allowances;
	}

	public void setAllowances(BigDecimal allowances) {
		this.allowances = allowances;
	}

	public BigDecimal getDeductions() {
		return deductions;
	}

	public void setDeductions(BigDecimal deductions) {
		this.deductions = deductions;
	}

	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public int getOvertimeHours() {
		return overtimeHours;
	}

	public void setOvertimeHours(int overtimeHours) {
		this.overtimeHours = overtimeHours;
	}

	public BigDecimal getOvertimeAmount() {
		return overtimeAmount;
	}

	public void setOvertimeAmount(BigDecimal overtimeAmount) {
		this.overtimeAmount = overtimeAmount;
	}

	public BigDecimal getBonusAmount() {
		return bonusAmount;
	}

	public void setBonusAmount(BigDecimal bonusAmount) {
		this.bonusAmount = bonusAmount;
	}

	public BigDecimal getGrossPay() {
		return grossPay;
	}

	public void setGrossPay(BigDecimal grossPay) {
		this.grossPay = grossPay;
	}

	public BigDecimal getNetPay() {
		return netPay;
	}

	public void setNetPay(BigDecimal netPay) {
		this.netPay = netPay;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public PayrollStatement() {

	}

	public PayrollStatement(long id, String empCode, String name, Date payrollMonth, BigDecimal basicPay,
			BigDecimal hra, BigDecimal allowances, BigDecimal deductions, BigDecimal tax, int overtimeHours,
			BigDecimal overtimeAmount, BigDecimal bonusAmount, BigDecimal grossPay, BigDecimal netPay, String status) {
		this.id = id;
		this.empCode = empCode;
		this.name = name;
		this.payrollMonth = payrollMonth;
		this.basicPay = basicPay;
		this.hra = hra;
		this.allowances = allowances;
		this.deductions = deductions;
		this.tax = tax;
		this.overtimeHours = overtimeHours;
		this.overtimeAmount = overtimeAmount;
		this.bonusAmount = bonusAmount;
		this.grossPay = grossPay;
		this.netPay = netPay;
		this.status = status;
	}

	@Override
	public String toString() {
		return "PayrollSummary [id=" + id + ", empCode=" + empCode + ", name=" + name + ", payrollMonth=" + payrollMonth
				+ ", basicPay=" + basicPay + ", hra=" + hra + ", allowances=" + allowances + ", deductions="
				+ deductions + ", tax=" + tax + ", overtimeHours=" + overtimeHours + ", overtimeAmount="
				+ overtimeAmount + ", bonusAmount=" + bonusAmount + ", grossPay=" + grossPay + ", netPay=" + netPay
				+ ", status=" + status + "]";
	}
}
