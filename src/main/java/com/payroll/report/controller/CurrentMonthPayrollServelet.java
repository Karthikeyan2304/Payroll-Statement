package com.payroll.report.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;

import com.google.gson.Gson;
import com.payroll.report.model.PayrollStatement;
import com.payroll.report.service.PayrollService;

/**
 * Servlet implementation class CurrentMonthPayrollServelet
 */
public class CurrentMonthPayrollServelet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private PayrollService payrollService;
 private final static Logger LOG=LoggerFactory.getLogger(CurrentMonthPayrollServelet.class);
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CurrentMonthPayrollServelet() {
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
		Calendar cal = Calendar.getInstance();
		String currentMonth=new SimpleDateFormat("YYYY-MM").format(cal.getTime());
		LOG.info("Current Month in Current Month Servlet {}: ",currentMonth);
		List<PayrollStatement> payrollList = null;
		try {
			payrollList = payrollService.getCurrentMonthPayroll(currentMonth);
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
