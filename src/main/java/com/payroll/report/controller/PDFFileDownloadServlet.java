package com.payroll.report.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.payroll.report.model.PayrollStatement;
import com.payroll.report.service.PayrollService;
import com.payroll.report.util.ExportPDFUtil;

/**
 * Servlet implementation class PDFFileDownloadServlet
 */
public class PDFFileDownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	TemplateEngine templateEngine;
	PayrollService payrollService;
	private static final Logger LOG = LoggerFactory.getLogger(PDFFileDownloadServlet.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PDFFileDownloadServlet() {
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
		String fileName = "Payroll";
		String[] headers = { "ID", "Employee Code", "Name", "Payroll Month", "Basic Pay", "HRA", "Allowance",
				"Deduction", "Tax", "Over Time", "Over Amount", "Bonus Amount", "Gross Pay", "Net Pay", "Status" };
		LOG.info("PDF TABLE Started");
		response.setContentType("application/pdf");

		ExportPDFUtil.exportPDF(fileName, headers, response, allPayroll, allPayrollFlag, month);

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
