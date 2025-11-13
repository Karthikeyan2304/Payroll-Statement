package com.payroll.report.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.payroll.report.model.PayrollStatement;
import com.payroll.report.service.PayrollService;

public class PayrollAllDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	TemplateEngine templateEngine;
	PayrollService payrollService;
	private static final Logger LOG = LoggerFactory.getLogger(PayrollAllDataServlet.class);

	@Override
	public void init() throws ServletException {
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
		WebContext context = new WebContext(request, response, getServletContext(), response.getLocale());
		response.setContentType("text/html;charset=UTF-8");
		// Mandatory
		HttpSession session = request.getSession();
		String username = session.getAttribute("loggedInUser").toString();
		String isAdmin = session.getAttribute("isAdmin").toString();
		String userType = session.getAttribute("userType").toString();
		context.setVariable("username", username);
		context.setVariable("isAdmin", Boolean.parseBoolean(isAdmin));
		context.setVariable("userType", userType);

		List<PayrollStatement> list = null;
		try {
			list = payrollService.getAllPayrolls(null, null);
		} catch (IOException e) {

			LOG.error("IOException in the doGet {} : ", e.getMessage(), e);
		} catch (SQLException e) {
			LOG.error("SQLException in the doGet {} : ", e.getMessage(), e);
		}
		if (list != null && list.size() > 0) {
			context.setVariable("allpayroll", list);

		} else {
			context.setVariable("no_data_found", "No data found");

		}
		templateEngine.process("allpayroll", context, response.getWriter());

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
