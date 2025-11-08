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
 * Servlet implementation class PreviousMonthPayrollServlet
 */
public class PreviousMonthPayrollServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	PayrollService payrollService;
	private final static Logger LOG = LoggerFactory.getLogger(PreviousMonthPayrollServlet.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PreviousMonthPayrollServlet() {
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
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		LocalDate currentDate = LocalDate.now();
		LocalDate previousMonthDate = currentDate.minusMonths(1);
		String formattedMonth = previousMonthDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
		LOG.info("Previous Month in PreviousMonthPayrollServlet {}: ", formattedMonth);
		List<PayrollStatement> payrollList = null;
		try {
			payrollList = payrollService.getPreviousMonthPayroll(formattedMonth);
		} catch (IOException | SQLException e) {
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
