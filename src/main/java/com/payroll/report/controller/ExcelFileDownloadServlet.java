package com.payroll.report.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.payroll.report.service.PayrollService;
import com.payroll.report.util.ExportExcelUtil;

public class ExcelFileDownloadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	TemplateEngine templateEngine;
	PayrollService payrollService;
	ExportExcelUtil exportExcelUtil;
	private final static Logger LOG = LoggerFactory.getLogger(ExcelFileDownloadServlet.class);

	public ExcelFileDownloadServlet() {
		super();

	}

	@Override
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

	public void setExportExcelUtil(ExportExcelUtil exportExcelUtil) {
		this.exportExcelUtil = exportExcelUtil;
	}

	@Override
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
		try {
			ExportExcelUtil.exportExcel(month, allPayroll, allPayrollFlag, response);
		} catch (Exception e) {
			LOG.error("Exception in the  doGet {} :", e.getMessage(), e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			doGet(request, response);
		} catch (Exception e) {
			LOG.error("Exception in the  doPost {} :", e.getMessage(), e);
		}
	}

	public void setPayrollService(PayrollService payrollService) {
		this.payrollService = payrollService;
	}

}
