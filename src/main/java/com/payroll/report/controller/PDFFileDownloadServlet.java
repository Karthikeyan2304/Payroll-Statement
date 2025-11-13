package com.payroll.report.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.payroll.report.service.PayrollService;
import com.payroll.report.util.ExportPDFUtil;

public class PDFFileDownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	TemplateEngine templateEngine;
	PayrollService payrollService;
	private static final Logger LOG = LoggerFactory.getLogger(PDFFileDownloadServlet.class);

	public PDFFileDownloadServlet() {
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

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String month = request.getParameter("month");
		String allPayroll = request.getParameter("allPayroll");
		Boolean allPayrollFlag = false;
		String fileName = "Payroll";
		String[] headers = { "ID", "Employee Code", "Name", "Payroll Month", "Basic Pay", "HRA", "Allowance",
				"Deduction", "Tax", "Over Time", "Over Amount", "Bonus Amount", "Gross Pay", "Net Pay", "Status" };
		LOG.info("PDF TABLE Started");
		response.setContentType("application/pdf");
       try {
		ExportPDFUtil.exportPDF(fileName, headers, response, allPayroll, allPayrollFlag, month);
       }
       catch(Exception e)
       {
    	LOG.error("Exception in the doGet {}",e.getMessage(),e);
       }
	}
    
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
