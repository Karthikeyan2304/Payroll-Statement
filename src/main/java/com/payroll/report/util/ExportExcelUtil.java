package com.payroll.report.util;

import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.payroll.report.model.PayrollStatement;
import com.payroll.report.service.PayrollService;

public class ExportExcelUtil {
	static PayrollService payrollService;
static {
	payrollService = new PayrollService();
}
	private ExportExcelUtil() {
		
	}

	public static void exportExcel(String month, String allPayroll, Boolean allPayrollFlag,
			HttpServletResponse response) throws ServletException {
		try (Workbook workbook = new XSSFWorkbook(); OutputStream out = response.getOutputStream()) {
			Sheet sheet = workbook.createSheet("Payroll " + month);
			Row header = sheet.createRow(0);
			String[] headers = { "ID", "Employee Code", "Name", "Payroll Month", "Basic Pay", "HRA", "Allowance",
					"Deduction", "Tax", "Over Time", "Over Amount", "Bonus Amount", "Gross Pay", "Net Pay", "Status" };
			for (int i = 0; i < headers.length; i++) {
				header.createCell(i).setCellValue(headers[i]);
			}
			List<PayrollStatement> list = null;
			if (StringUtils.isNotBlank(month)) {
				list = payrollService.getPayrollsMonthWise(month);
			}
			if (allPayrollFlag) {
				response.setHeader("Content-Disposition", "attachment; filename=AllPayroll.xlsx");

				list = payrollService.getAllPayrolls(null, null);
			}
			int rowNum = 1;
			if (list != null) {
				for (PayrollStatement p : list) {
					Row row = sheet.createRow(rowNum++);
					row.createCell(0).setCellValue(p.getId());
					row.createCell(1).setCellValue(p.getEmpCode());
					row.createCell(2).setCellValue(p.getName());
					row.createCell(3).setCellValue(p.getPayrollMonth().toString());
					row.createCell(4).setCellValue(p.getBasicPay().doubleValue());
					row.createCell(5).setCellValue(p.getHra().doubleValue());
					row.createCell(6).setCellValue(p.getAllowances().doubleValue());
					row.createCell(7).setCellValue(p.getDeductions().doubleValue());
					row.createCell(8).setCellValue(p.getTax().doubleValue());
					row.createCell(9).setCellValue(p.getOvertimeHours());
					row.createCell(10).setCellValue(p.getOvertimeAmount().doubleValue());
					row.createCell(11).setCellValue(p.getBonusAmount().doubleValue());
					row.createCell(12).setCellValue(p.getGrossPay().doubleValue());
					row.createCell(13).setCellValue(p.getNetPay().doubleValue());
					row.createCell(14).setCellValue(p.getStatus());
				}

				for (int i = 0; i < headers.length; i++) {
					sheet.autoSizeColumn(i);
				}
				workbook.write(out);
			}
		} catch (Exception e) {
			throw new ServletException("Error generating Excel report", e);
		}
	}
}
