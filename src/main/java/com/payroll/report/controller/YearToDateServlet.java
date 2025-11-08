package com.payroll.report.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.payroll.report.model.PayrollStatement;
import com.payroll.report.service.PayrollService;

/**
 * Servlet implementation class YearToDateServlet
 */
public class YearToDateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static Logger LOG = LoggerFactory.getLogger(YearToDateServlet.class);
	PayrollService payrollService;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public YearToDateServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() throws ServletException {
		payrollService = new PayrollService();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LocalDate today = LocalDate.now();
		LocalDate startOfYear = today.withDayOfYear(1);
		LocalDate endOfYearToDate = today;
		String startOfYearDate = startOfYear.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String endOfYearDate = endOfYearToDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		LOG.info("YearToDateServlet Date  {}: {}", startOfYearDate, endOfYearDate);
		List<PayrollStatement> payrollList = null;
		try {
			payrollList = payrollService.getYTPPayroll(startOfYearDate, endOfYearDate);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Gson gson = new Gson();
		String jsonResponse = gson.toJson(payrollList);
		response.getWriter().write(jsonResponse);
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
