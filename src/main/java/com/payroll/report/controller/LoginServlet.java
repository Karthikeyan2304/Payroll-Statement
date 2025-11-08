package com.payroll.report.controller;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.payroll.report.service.PayrollService;
import com.payroll.report.service.UserService;
import com.payroll.report.util.ClientProperties;
import com.payroll.report.util.DatabaseUtil;

public class LoginServlet extends javax.servlet.http.HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2562418503227548100L;
	private TemplateEngine templateEngine;
	private PayrollService payrollService;
	private UserService userService;

	@Override
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
		userService = new UserService();

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		WebContext context = new WebContext(req, resp, getServletContext(), resp.getLocale());
		resp.setContentType("text/html;charset=UTF-8");
		templateEngine.process("login", context, resp.getWriter());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String userName = req.getParameter("username");
		String passWord = req.getParameter("password");
		String phoneNumber = null;
		boolean otpFlag = false;
		WebContext context = new WebContext(req, resp, getServletContext(), resp.getLocale());
		boolean isValidUser = false;

		try {
			isValidUser = userService.checkValidUser(userName, passWord);
			phoneNumber = userService.getUserMobile(userName);
			otpFlag = Boolean.parseBoolean(ClientProperties.getProperty("otp.flag").trim());
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}

		if (isValidUser && otpFlag == false) {
			HttpSession session = req.getSession(true);
			session.setAttribute("loggedInUser", userName);
			context.setVariable("username", userName);
			// Cookie
			Cookie userCookie = new Cookie("userName", userName);
			userCookie.setMaxAge(5 * 60);
			userCookie.setPath("/");
			userCookie.setHttpOnly(true);
			userCookie.setSecure(true);
			resp.addCookie(userCookie);
			//
			Boolean isAdmin = false;
			try {
				isAdmin = userService.getUserType(userName);
				if (isAdmin == true) {
					session.setAttribute("isAdmin", isAdmin);
					session.setAttribute("userType", "Admin");
					context.setVariable("isAdmin", isAdmin);
					context.setVariable("userType", "Admin");
					Timestamp currentLoginDTTM = Timestamp.from(Instant.now());
					String userID;
					try {
						userID = DatabaseUtil.getUserID(userName);
						DatabaseUtil.saveUserLoginInDB(userID, userName, "Admin", currentLoginDTTM, "N");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					session.setAttribute("isAdmin", isAdmin);
					session.setAttribute("userType", "User");
					context.setVariable("isAdmin", isAdmin);
					context.setVariable("userType", "User");
					Timestamp currentLoginDTTM = Timestamp.from(Instant.now());
					String userID;
					try {
						userID = DatabaseUtil.getUserID(userName);
						DatabaseUtil.saveUserLoginInDB(userID, userName, "User", currentLoginDTTM, "N");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			resp.sendRedirect("welcome");
			templateEngine.process("welcome", context, resp.getWriter());
		}
		// OTP TESTING
		else if (isValidUser && otpFlag == true) {
			HttpSession session = req.getSession(true);
			session.setAttribute("loggedInUser", userName);
			// Cookie
			Cookie userCookie = new Cookie("userName", userName);
			userCookie.setMaxAge(5 * 60);
			userCookie.setPath("/");
			userCookie.setHttpOnly(true);
			userCookie.setSecure(true);
			resp.addCookie(userCookie);

			try {

				// Testing
				if (phoneNumber == null || phoneNumber.isEmpty()) {
					phoneNumber = "+918608041997";
				}
				// -----------------------------
				userService.sendOTPToUser(phoneNumber);
				Boolean isAdmin = false;
				isAdmin = userService.getUserType(userName);
				if (isAdmin == true) {
					session.setAttribute("isAdmin", isAdmin);
					session.setAttribute("userType", "Admin");
					context.setVariable("isAdmin", isAdmin);
					context.setVariable("userType", "Admin");
					Timestamp currentLoginDTTM = Timestamp.from(Instant.now());
					String userID;
					try {
						userID = DatabaseUtil.getUserID(userName);
						DatabaseUtil.saveUserLoginInDB(userID, userName, "Admin", currentLoginDTTM, "Y");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					session.setAttribute("isAdmin", isAdmin);
					session.setAttribute("userType", "User");
					context.setVariable("isAdmin", isAdmin);
					context.setVariable("userType", "User");
					Timestamp currentLoginDTTM = Timestamp.from(Instant.now());
					String userID;
					try {
						userID = DatabaseUtil.getUserID(userName);
						DatabaseUtil.saveUserLoginInDB(userID, userName, "User", currentLoginDTTM, "Y");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				context.setVariable("showOTPField", true);
				context.setVariable("username", userName);
				context.setVariable("phoneNumber", phoneNumber);
				templateEngine.process("login", context, resp.getWriter());
				return;

			}

			catch (IOException | SQLException e) {

				e.printStackTrace();
			}

		} else {
			context.setVariable("error", "Invalid username or password");
			templateEngine.process("login", context, resp.getWriter());
		}
	}
}
