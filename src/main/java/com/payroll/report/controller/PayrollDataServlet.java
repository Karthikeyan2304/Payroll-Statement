package com.payroll.report.controller;

import com.payroll.report.model.PayrollStatement;
import com.payroll.report.service.PayrollService;
import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.http.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

public class PayrollDataServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private PayrollService payrollService;
	private final Gson gson = new Gson();
	private final static Logger LOG = LoggerFactory.getLogger(PayrollDataServlet.class);

	@Override
	public void init() throws ServletException {
		payrollService = new PayrollService();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("application/json");
		String type = req.getParameter("type"); // "YTD" | "CURRENT" | "PREVIOUS"
		List<PayrollStatement> payrollList = null;

		try {
			switch (type != null ? type.toUpperCase() : "") {
			case "YTD":
				LocalDate today = LocalDate.now();
				LocalDate startOfYear = today.withDayOfYear(1);
				LocalDate endOfYearToDate = today;
				String startOfYearDate = startOfYear.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				String endOfYearDate = endOfYearToDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				LOG.info("YearToDateServlet Date in PayrollDataServlet {}: {}", startOfYearDate, endOfYearDate);
				payrollList = payrollService.getYTPPayroll(startOfYearDate, endOfYearDate);
				break;
			case "CURRENT":
				Calendar cal = Calendar.getInstance();
				String currentMonth = new SimpleDateFormat("yyyy-MM").format(cal.getTime());
				LOG.info("Current Month in PayrollDataServlet  {}: ", currentMonth);
				payrollList = payrollService.getCurrentMonthPayroll(currentMonth);
				break;
			case "PREVIOUS":
				LocalDate currentDate = LocalDate.now();
				LocalDate previousMonthDate = currentDate.minusMonths(1);
				String formattedMonth = previousMonthDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
				LOG.info("Previous Month in PayrollDataServlet {}: ", formattedMonth);
				payrollList = payrollService.getPreviousMonthPayroll(formattedMonth);
				break;
			default:
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				resp.getWriter().write("{\"error\":\"Invalid payroll type\"}");
				return;
			}

			String json = gson.toJson(payrollList);
			resp.getWriter().write(json);

		} catch (Exception e) {
			LOG.error("Exception in the doGet {} : ", e.getMessage(), e);
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().write("{\"error\":\"Server error\"}");
		}
	}
}
