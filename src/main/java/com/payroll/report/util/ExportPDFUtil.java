package com.payroll.report.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
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
import com.itextpdf.text.Rectangle;
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

		response.setContentType("application/pdf");
		Document doc = new Document();
		try {
			PdfWriter.getInstance(doc, response.getOutputStream());
			doc.open();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (StringUtils.isNotBlank(allPayroll)) {
			try {
				doc.add(new Paragraph("Overall Payroll Data"));
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			fileName = "allPayroll.pdf";
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			allPayrollFlag = Boolean.parseBoolean(allPayroll);

		}
		List<PayrollStatement> list = null;
		if (StringUtils.isNotBlank(month)) {
			try {
				doc.add(new Paragraph("Monthly Payroll Data"));
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			fileName = "Payroll_" + month + ".pdf";
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

			try {
				list = payrollService.getPayrollsMonthWise(month);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (allPayrollFlag) {
			try {
				list = payrollService.getAllPayrolls(null, null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		PdfPTable table = new PdfPTable(headers.length);
		table.setWidthPercentage(105);
		table.getDefaultCell().setBorder(Rectangle.BOX);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE); 
		for (int i = 0; i < headers.length; i++) {
			PdfPCell headerCell = new PdfPCell(new Phrase(headers[i]));
			headerCell.setBackgroundColor(BaseColor.GRAY);
			
			table.addCell(headerCell);
		}

		for (PayrollStatement p : list) {
			table.addCell(String.valueOf(p.getId()));
			table.addCell(String.valueOf(p.getEmpCode()));
			table.addCell(p.getName());
			table.addCell(String.valueOf(p.getPayrollMonth()));
			table.addCell(String.valueOf(p.getBasicPay().doubleValue()));
			table.addCell(String.valueOf(p.getHra().doubleValue()));
			table.addCell(String.valueOf(p.getAllowances().doubleValue()));
			table.addCell(String.valueOf(p.getDeductions().doubleValue()));
			table.addCell(String.valueOf(p.getTax().doubleValue()));
			table.addCell(String.valueOf(p.getOvertimeHours()));
			table.addCell(String.valueOf(p.getOvertimeAmount().doubleValue()));
			table.addCell(String.valueOf(p.getBonusAmount().doubleValue()));
			table.addCell(String.valueOf(p.getGrossPay().doubleValue()));
			table.addCell(String.valueOf(p.getNetPay().doubleValue()));
			table.addCell(String.valueOf(p.getStatus()));
		}
		try {
			doc.add(table);
			doc.close();
			LOG.info("PDF TABLE Ended");
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
