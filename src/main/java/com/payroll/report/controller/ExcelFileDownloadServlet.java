package com.payroll.report.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.payroll.report.model.PayrollStatement;
import com.payroll.report.service.PayrollService;
import com.payroll.report.util.ExportExcelUtil;

/**
 * Servlet implementation class ExcelFileDownloadServlet
 */
public class ExcelFileDownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	TemplateEngine templateEngine;
	PayrollService payrollService;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ExcelFileDownloadServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init() throws javax.servlet.ServletException {
		ClassLoaderTemplateResolver classLoaderTemp = new ClassLoaderTemplateResolver();
		classLoaderTemp.setPrefix("templates/");
		classLoaderTemp.setSuffix(".html");
		classLoaderTemp.setTemplateMode("HTML");
		classLoaderTemp.setCharacterEncoding("UTF-8");
		classLoaderTemp.setCacheable(false);
		templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(classLoaderTemp);
		payrollService = new PayrollService();

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String month = request.getParameter("month");
		String allPayroll = request.getParameter("allPayroll");
		Boolean allPayrollFlag = false;
		if (StringUtils.isNotBlank(allPayroll)) {
			allPayrollFlag = Boolean.parseBoolean(allPayroll);

		}
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

		response.setHeader("Content-Disposition", "attachment; filename=Payroll_" + month + ".xlsx");

		ExportExcelUtil.exportExcel(month, allPayroll, allPayrollFlag, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
