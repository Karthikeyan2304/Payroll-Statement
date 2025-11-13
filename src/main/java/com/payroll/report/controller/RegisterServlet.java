package com.payroll.report.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.payroll.report.util.DatabaseUtil;
import com.payroll.report.util.DuplicateUserException;
import com.payroll.report.util.PayrollReportHasher;

public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static TemplateEngine templateEngine;
	private static final Logger LOG = LoggerFactory.getLogger(RegisterServlet.class);

	public RegisterServlet() {
		super();

	}

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
	}

	@Override

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		WebContext context = new WebContext(request, response, getServletContext(), response.getLocale());
		response.setContentType("text/html;charset=UTF-8");

		HttpSession session = request.getSession(false);
		String isAdmin = session.getAttribute("isAdmin").toString();
		if (Boolean.parseBoolean(isAdmin) == true) {
			String username = session.getAttribute("loggedInUser").toString();
			context.setVariable("isAdmin", Boolean.parseBoolean(isAdmin));
			context.setVariable("userType", "Admin");
			context.setVariable("username", username);

			templateEngine.process("registerUser", context, response.getWriter());
		} else {
			String username = session.getAttribute("loggedInUser").toString();
			context.setVariable("isAdmin", Boolean.parseBoolean(isAdmin));
			context.setVariable("userType", "User");
			context.setVariable("username", username);
			templateEngine.process("welcome", context, response.getWriter());
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		String userName = request.getParameter("username");
		String passWord = request.getParameter("password");
		String userID = request.getParameter("userid");
		String mobileno = request.getParameter("mobileno");
		String userType = request.getParameter("role");

		WebContext context = new WebContext(request, response, getServletContext(), response.getLocale());
		if (StringUtils.isNoneBlank(passWord) && StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(mobileno)
				&& StringUtils.isNotBlank(userType)) {
			String hashedPassword = PayrollReportHasher.passwordHasher(passWord);
			try {
				DatabaseUtil.saveUserDetails(userID, userName, mobileno, hashedPassword, userType);
				context.setVariable("usersuccess", "User Created Successfully");
				templateEngine.process("registerUser", context, response.getWriter());
			} catch (DuplicateUserException e) {
				context.setVariable("error", "User already present");
				templateEngine.process("registerUser", context, response.getWriter());
				LOG.error("DuplicateUserException in the doPost {} : ", e.getMessage(), e);
			} catch (Exception e) {
				LOG.error("Exception in the doPost {} : ", e.getMessage(), e);

				context.setVariable("error", "Please enter username or password");
				templateEngine.process("registerUser", context, response.getWriter());

			}

		} else {
			context.setVariable("error", "Please check the form");
			templateEngine.process("registerUser", context, response.getWriter());
		}
	}
}
