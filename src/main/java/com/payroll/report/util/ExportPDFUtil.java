package com.payroll.report.util;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.payroll.report.model.PayrollStatement;
import com.payroll.report.service.PayrollService;

public class ExportPDFUtil {
	static PayrollService payrollService;
	static {
		payrollService = new PayrollService();
	}

	private ExportPDFUtil() {

	}

	private static final Logger LOG = LoggerFactory.getLogger(ExportPDFUtil.class);

	public static void exportPDF(String fileName, String[] headers, HttpServletResponse response, String allPayroll,
			Boolean allPayrollFlag, String month) throws IOException {

// Determine PDF file name
		String pdfFileName = "PayrollReport.pdf";
		if (StringUtils.isNotBlank(allPayroll)) {
			pdfFileName = "allPayroll.pdf";
			allPayrollFlag = Boolean.parseBoolean(allPayroll);
		} else if (StringUtils.isNotBlank(month)) {
			pdfFileName = "Payroll_" + month + ".pdf";
		}
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=" + pdfFileName);

// Fetch payroll data
		List<PayrollStatement> list = Collections.emptyList();
		try {
			if (allPayrollFlag) {
				list = payrollService.getAllPayrolls(null, null);
			} else if (StringUtils.isNotBlank(month)) {
				list = payrollService.getPayrollsMonthWise(month);
			}
		} catch (IOException | SQLException e) {
			LOG.error("Error fetching payroll data for PDF: {}", e.getMessage(), e);
		}

		Document doc = new Document();
		try {
			PdfWriter.getInstance(doc, response.getOutputStream());
			doc.open();

			String title = StringUtils.isNotBlank(allPayroll) ? "Overall Payroll Data" : "Monthly Payroll Data";
			doc.add(new Paragraph(title));
			doc.add(new Paragraph(" ")); // empty line for spacing
			PdfPTable table = new PdfPTable(headers.length);
			table.setWidthPercentage(100); // 100% width
			table.setSpacingBefore(10f);
			table.setSpacingAfter(10f);

			for (String header : headers) {
				PdfPCell headerCell = new PdfPCell(new Phrase(header));
				headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
				headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(headerCell);
			}

			for (PayrollStatement p : list) {
				table.addCell(String.valueOf(p.getId()));
				table.addCell(String.valueOf(p.getEmpCode()));
				table.addCell(p.getName() != null ? p.getName() : "");
				table.addCell(p.getPayrollMonth() != null ? String.valueOf(p.getPayrollMonth()) : "");
				table.addCell(p.getBasicPay() != null ? String.format("%.2f", p.getBasicPay()) : "0.00");
				table.addCell(p.getHra() != null ? String.format("%.2f", p.getHra()) : "0.00");
				table.addCell(p.getAllowances() != null ? String.format("%.2f", p.getAllowances()) : "0.00");
				table.addCell(p.getDeductions() != null ? String.format("%.2f", p.getDeductions()) : "0.00");
				table.addCell(p.getTax() != null ? String.format("%.2f", p.getTax()) : "0.00");
				table.addCell(String.valueOf(p.getOvertimeHours()));
				table.addCell(p.getOvertimeAmount() != null ? String.format("%.2f", p.getOvertimeAmount()) : "0.00");
				table.addCell(p.getBonusAmount() != null ? String.format("%.2f", p.getBonusAmount()) : "0.00");
				table.addCell(p.getGrossPay() != null ? String.format("%.2f", p.getGrossPay()) : "0.00");
				table.addCell(p.getNetPay() != null ? String.format("%.2f", p.getNetPay()) : "0.00");
				table.addCell(p.getStatus() != null ? p.getStatus() : "");
			}

			doc.add(table);
			LOG.info("PDF export completed successfully: {}", pdfFileName);

		} catch (DocumentException | IOException e) {
			LOG.error("Exception while generating PDF {}: {}", pdfFileName, e.getMessage(), e);
			throw new IOException("Error generating PDF: " + e.getMessage(), e);
		} finally {
			if (doc.isOpen()) {
				doc.close();
			}
		}
	}
}
