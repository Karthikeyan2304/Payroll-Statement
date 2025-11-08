package com.payroll.report.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.payroll.report.util.DatabaseUtil;
import com.payroll.report.util.DuplicateUserException;
import com.payroll.report.util.PayrollReportHasher;

/**
 * Servlet implementation class RegisterServlet
 */
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterServlet() {
		super();
		// TODO Auto-generated constructor stub
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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		String userName = request.getParameter("username");
		String passWord = request.getParameter("password");
		String userID = request.getParameter("userid");
		String mobileno = request.getParameter("mobileno");
		String userType = request.getParameter("role");
		// HttpSession session = request.getSession();
//		session.setAttribute("userid", userID);
//		session.setAttribute("userName", userName);
//		session.setAttribute("mobileno", mobileno);
//		session.setAttribute("passWord", passWord);
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
				// throw new DuplicateUserException("User already present");
			} catch (Exception e) {

				// TODO Auto-generated catch block
				context.setVariable("error", "Please enter username or password");
				templateEngine.process("registerUser", context, response.getWriter());
				e.printStackTrace();
			}

		} else {
			context.setVariable("error", "Please check the form");
			templateEngine.process("registerUser", context, response.getWriter());
		}
	}
}
